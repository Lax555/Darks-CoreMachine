package dark.core.registration;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.core.common.DarkMain;
import dark.core.prefab.ModPrefab;

public class BlockFluid extends BlockFluidFinite
{
    Icon flowing;
    Icon still;
    Fluid fluid;
    String prefix = DarkMain.getInstance().PREFIX;

    public BlockFluid(String prefix, Fluid fluid, Configuration config)
    {
        super(config.getBlock("BlockFluid" + fluid.getName(), ModPrefab.getNextID()).getInt(), fluid, Material.water);
        this.fluid = fluid;
        if (prefix != null && prefix.contains(":"))
        {
            this.prefix = prefix;
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.flowing = par1IconRegister.registerIcon(prefix + this.getUnlocalizedName().replace("tile.", "") + "_flowing");
        this.still = par1IconRegister.registerIcon(prefix + this.getUnlocalizedName().replace("tile.", "") + "_still");
        fluid.setIcons(still, flowing);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return still;
    }

}
