/**
 * Code by: https://github.com/alewalls17
 *     Code under MIT License
 * <p>
 *     MIT License
 * <p>
 *         Copyright (c) 2023 alewalls17
 * <p>
 *         Permission is hereby granted, free of charge, to any person obtaining a copy
 *         of this software and associated documentation files (the "Software"), to deal
 *         in the Software without restriction, including without limitation the rights
 *         to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *         copies of the Software, and to permit persons to whom the Software is
 *         furnished to do so, subject to the following conditions:
 * <p>
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 * <p>
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *         FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *         AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *         LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *         OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *         SOFTWARE.
 */

package ua.pp.lumivoid.redstonehelper.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ua.pp.lumivoid.redstonehelper.registration.GamerulesRegistration;

import java.util.Objects;

@Mixin(RedstoneLampBlock.class)
public abstract class RedstoneLampMixin extends Block {

    public RedstoneLampMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "neighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    public void redirectScheduleBlockTick(World world, BlockPos pos, Block block, int delay) {
        if (Objects.requireNonNull(world.getServer()).getGameRules().getBoolean(GamerulesRegistration.INSTANCE.getINSTANT_LAMPS_TURN_OFF())) {
            world.scheduleBlockTick(pos, block, 0);
        } else {
            world.scheduleBlockTick(pos, block, delay);
        }
    }
}
