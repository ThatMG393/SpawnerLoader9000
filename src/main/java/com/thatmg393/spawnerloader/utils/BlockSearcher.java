package com.thatmg393.spawnerloader.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;

public class BlockSearcher {
    // Region size constants (powers of 2)
    private static final int REGION_WIDTH_BITS = 4;   // 16 = 2^4
    private static final int REGION_HEIGHT_BITS = 5;  // 32 = 2^5
    private static final int REGION_LENGTH_BITS = 4;  // 16 = 2^4
    
    private static final int REGION_WIDTH = 1 << REGION_WIDTH_BITS;
    private static final int REGION_HEIGHT = 1 << REGION_HEIGHT_BITS;
    private static final int REGION_LENGTH = 1 << REGION_LENGTH_BITS;
    
    private static final int REGION_HEIGHT_MASK = REGION_HEIGHT - 1;

    // Thread pool configuration
    private static final int THREAD_POOL_SIZE = 16;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     * Asynchronously checks if a specific block appears twice within a square region of chunks.
     * @return CompletableFuture<Boolean> that completes with true if block is found twice
     */
    public static CompletableFuture<Boolean> isBlockPresent(World world, BlockPos centerPos, Block targetBlock, int chunkRadius) {
        if ((chunkRadius & 1) == 0) {
            return CompletableFuture.failedFuture(
                new IllegalArgumentException("Chunk radius must be odd!")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                ChunkPos centerChunk = new ChunkPos(centerPos);
                int halfRadius = chunkRadius >> 1;
                AtomicInteger totalFound = new AtomicInteger(0);
                List<CompletableFuture<Void>> searchFutures = new ArrayList<>();

                // Create all search tasks
                for (int cx = -halfRadius; cx <= halfRadius; cx++) {
                    for (int cz = -halfRadius; cz <= halfRadius; cz++) {
                        int chunkX = centerChunk.x + cx;
                        int chunkZ = centerChunk.z + cz;
                        
                        // Search this chunk
                        CompletableFuture<Void> chunkFuture = searchChunkParallel(
                            world, chunkX, chunkZ, targetBlock, totalFound
                        );
                        searchFutures.add(chunkFuture);
                    }
                }

                // Wait for all searches to complete or until we find 2 blocks
                CompletableFuture.allOf(searchFutures.toArray(new CompletableFuture[0])).join();
                return totalFound.get() >= 2;

            } catch (Exception e) {
                // If we get here due to cancellation, it means we found 2+ blocks
                return true;
            }
        }, EXECUTOR);
    }

    /**
     * Searches a chunk in parallel regions, using the thread pool
     */
    private static CompletableFuture<Void> searchChunkParallel(
            World world, 
            int chunkX, 
            int chunkZ, 
            Block targetBlock,
            AtomicInteger totalFound) {
        
        int startX = chunkX << REGION_WIDTH_BITS;
        int startZ = chunkZ << REGION_LENGTH_BITS;
        
        int minY = world.getBottomY();
        int maxY = world.getTopY();
        int regionCount = (maxY - minY + REGION_HEIGHT_MASK) >> REGION_HEIGHT_BITS;
        
        List<CompletableFuture<Void>> regionFutures = new ArrayList<>();

        // Create search tasks for each region
        for (int i = 0; i < regionCount; i++) {
            int yOffset = i << REGION_HEIGHT_BITS;
            int finalY = minY + yOffset;
            
            if (finalY >= maxY) break;

            // Create a task for this region
            CompletableFuture<Void> regionFuture = CompletableFuture.supplyAsync(() -> {
                int count = searchRegion(
                    world,
                    startX,
                    finalY,
                    startZ,
                    Math.min(maxY - finalY, REGION_HEIGHT),
                    targetBlock
                );

                // Update total and potentially trigger early completion
                int newTotal = totalFound.addAndGet(count);
                if (newTotal >= 2) {
                    regionFutures.forEach(f -> f.cancel(true));
                }
                return null;
            }, EXECUTOR);

            regionFutures.add(regionFuture);
        }

        // Return a future that completes when all regions are searched
        return CompletableFuture.allOf(regionFutures.toArray(new CompletableFuture[0]));
    }

    /**
     * Searches a specific 3D region for the target block
     */
    private static int searchRegion(
            World world,
            int startX,
            int startY,
            int startZ,
            int height,
            Block targetBlock) {
        
        int count = 0;
        int endX = startX + REGION_WIDTH;
        int endZ = startZ + REGION_LENGTH;
        int endY = startY + height;
        
        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                for (int y = startY; y < endY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() == targetBlock) {
                        if (++count >= 2) return count;
                    }
                }
            }
        }
        
        return count;
    }

    /**
     * Shuts down the thread pool - call this when your mod/plugin unloads
     */
    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}