package dark.core.common.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.tools.IToolWrench;
import dark.core.common.DarkMain;
import dark.core.prefab.items.ItemBasic;

public class ItemWrench extends ItemBasic implements IToolWrench
{
    static boolean damageWrench = false;

    public ItemWrench(int id, Configuration config)
    {
        super(id, "wrench", config);
        damageWrench = config.get("general", "DamageWrench", false).getBoolean(false);
        this.setMaxStackSize(1);
        this.setMaxDamage(500 + config.get("general", "AddedWrenchUses", 500).getInt());
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setTextureName(DarkMain.getInstance().PREFIX + "wrench");
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        int blockID = world.getBlockId(x, y, z);

        if (blockID == Block.furnaceIdle.blockID || blockID == Block.furnaceBurning.blockID || blockID == Block.dropper.blockID || blockID == Block.hopperBlock.blockID || blockID == Block.dispenser.blockID || blockID == Block.pistonBase.blockID || blockID == Block.pistonStickyBase.blockID)
        {
            int metadata = world.getBlockMetadata(x, y, z);

            int[] rotationMatrix = { 1, 2, 3, 4, 5, 0 };

            if (blockID == Block.furnaceIdle.blockID || blockID == Block.furnaceBurning.blockID)
            {
                rotationMatrix = ForgeDirection.ROTATION_MATRIX[0];
            }

            world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(rotationMatrix[metadata]).ordinal(), 3);
            this.wrenchUsed(entityPlayer, x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean canWrench(EntityPlayer player, int x, int y, int z)
    {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, int x, int y, int z)
    {
        if (damageWrench && player != null && !player.worldObj.isRemote)
        {
            ItemStack stack = player.getHeldItem();
            if (stack != null && stack.itemID == this.itemID)
            {
                stack.damageItem(1, player);
            }
        }

    }
}
