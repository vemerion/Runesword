package mod.vemerion.runesword.block;

import mod.vemerion.runesword.blockentity.RuneforgeBlockEntity;
import mod.vemerion.runesword.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class RuneforgeBlock extends Block implements EntityBlock {
	private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

	public RuneforgeBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		var blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof MenuProvider menuProvider)
			return menuProvider;
		return null;
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
			BlockHitResult pHit) {
		if (!pLevel.isClientSide) {
			var blockEntity = pLevel.getBlockEntity(pPos);
			if (blockEntity instanceof MenuProvider menuProvider) {
				NetworkHooks.openGui((ServerPlayer) pPlayer, menuProvider);
			}
		}
		return InteractionResult.sidedSuccess(pLevel.isClientSide);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			var blockEntity = level.getBlockEntity(pos);
			if (blockEntity != null) {
				blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					if (handler.getSlots() > 0) {
						ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(),
								handler.extractItem(RuneforgeBlockEntity.RUNEABLE_SLOT, 1, false));
						level.addFreshEntity(item);
					}
				});
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new RuneforgeBlockEntity(pPos, pState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
			BlockEntityType<T> pBlockEntityType) {
		return pBlockEntityType == ModBlockEntities.RUNEFORGE.get()
				? (level, pos, state, tileEntity) -> ((RuneforgeBlockEntity) tileEntity).tick()
				: null;
	}

}
