package dark.core.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.core.common.DarkMain;

public class ItemColoredDust extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon theIcon;

    public ItemColoredDust(int par1, String name)
    {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setUnlocalizedName(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return DarkMain.dyeColors[par1ItemStack.getItemDamage() % 16].getRGB();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(DarkMain.getInstance().PREFIX + "dust");
        this.theIcon = par1IconRegister.registerIcon("glowingPowder_overlay");
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public final String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return this.getUnlocalizedName() + "." + DarkMain.dyeColorNames[par1ItemStack.getItemDamage()];
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < DarkMain.dyeColorNames.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
}
