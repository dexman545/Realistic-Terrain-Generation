package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.world.RTGWorld;
import rtg.api.config.BiomeConfig;
import rtg.api.util.CliffCalculator;
import rtg.world.biome.deco.collection.DecoCollectionTaiga;
import rtg.world.gen.surface.SurfaceBase;
import rtg.world.gen.terrain.TerrainBase;

public class RealisticBiomeVanillaColdTaigaM extends RealisticBiomeVanillaBase {

    public static Biome biome = Biomes.MUTATED_TAIGA_COLD;
    public static Biome river = Biomes.FROZEN_RIVER;

    public RealisticBiomeVanillaColdTaigaM() {

        super(biome, river);

        this.noLakes = true;
    }

    @Override
    public void initConfig() {

        this.getConfig().addProperty(this.getConfig().ALLOW_LOGS).set(true);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaColdTaigaM();
    }

    public class TerrainVanillaColdTaigaM extends TerrainBase {

        public TerrainVanillaColdTaigaM() {

        }

        @Override
        public float generateNoise(RTGWorld rtgWorld, int x, int y, float border, float river) {

            return terrainGrasslandMountains(x, y, rtgWorld.simplex, rtgWorld.cell, river, 4f, 80f, 68f);
        }
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaColdTaigaM(config, biome.topBlock, biome.fillerBlock);
    }

    public class SurfaceVanillaColdTaigaM extends SurfaceBase {

        public SurfaceVanillaColdTaigaM(BiomeConfig config, IBlockState top, IBlockState filler) {

            super(config, top, filler);
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

            Random rand = rtgWorld.rand;
            float c = CliffCalculator.calc(x, z, noise);
            boolean cliff = c > 1.4f ? true : false;

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (cliff) {
                        if (depth > -1 && depth < 2) {
                            if (rand.nextInt(3) == 0) {

                                primer.setBlockState(x, k, z, hcCobble(rtgWorld, i, j, x, z, k));
                            }
                            else {

                                primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                            }
                        }
                        else if (depth < 10) {
                            primer.setBlockState(x, k, z, hcStone(rtgWorld, i, j, x, z, k));
                        }
                    }
                    else {
                        if (depth == 0 && k > 61) {
                            primer.setBlockState(x, k, z, topBlock);
                        }
                        else if (depth < 4) {
                            primer.setBlockState(x, k, z, fillerBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initDecos() {

        this.addDecoCollection(new DecoCollectionTaiga(this.getConfig().ALLOW_LOGS.get(), 8f));
    }
}
