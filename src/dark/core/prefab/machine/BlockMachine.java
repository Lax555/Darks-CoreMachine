package dark.core.prefab.machine;

import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import universalelectricity.prefab.block.BlockTile;

import com.builtbroken.common.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.api.parts.INetworkPart;
import dark.core.common.DarkMain;
import dark.core.interfaces.IBlockActivated;
import dark.core.prefab.IExtraInfo.IExtraBlockInfo;
import dark.core.prefab.ModPrefab;
import dark.core.registration.ModObjectRegistry.BlockBuildData;

/** Basic TileEntity Container class designed to be used by generic machines. It is suggested that
 * each mod using this create there own basic block extending this to reduce need to use build data
 * per block.
 * 
 * @author Darkguardsman */
public abstract class BlockMachine extends BlockTile implements IExtraBlockInfo
{

    public boolean zeroAnimation, zeroSound, zeroRendering;

    public BlockMachine(BlockBuildData data)
    {
        super(data.config.getBlock(data.blockName, ModPrefab.getNextID()).getInt(), data.blockMaterial);
        this.setUnlocalizedName(data.blockName);
        this.setResistance(100f);
        if (data.creativeTab != null)
        {
            this.setCreativeTab(data.creativeTab);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
        if (this.blockIcon == null)
        {
            this.blockIcon = iconReg.registerIcon(DarkMain.getInstance().PREFIX + "machine");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        return this.blockIcon;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return this.zeroRendering;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock()
    {
        return this.zeroRendering;
    }

    /** Called whenever the block is added into the world. Args: world, x, y, z */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity instanceof INetworkPart)
        {
            ((INetworkPart) tileEntity).refresh();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        super.onNeighborBlockChange(world, x, y, z, blockID);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof INetworkPart)
        {
            ((INetworkPart) tileEntity).refresh();
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return super.createTileEntity(world, metadata);
    }

    @Override
    public void getTileEntities(int blockID, Set<Pair<String, Class<? extends TileEntity>>> list)
    {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getClientTileEntityRenderers(List<Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer>> list)
    {

    }

    @Override
    public boolean hasExtraConfigs()
    {
        return true;
    }

    @Override
    public void loadExtraConfigs(Configuration config)
    {
        this.zeroAnimation = config.get("Effects--Not_Supported_By_All_Blocks", "disableAnimation", false, "Turns off animations of the block").getBoolean(false);
        this.zeroRendering = config.get("Effects--Not_Supported_By_All_Blocks", "disableRender", false, "Turns off the block render replacing it with a normal block").getBoolean(false);
        this.zeroSound = config.get("Effects--Not_Supported_By_All_Blocks", "disableSound", false, "Turns of sound of the block for any or its actions").getBoolean(false);
    }

    @Override
    public void loadOreNames()
    {
        OreDictionary.registerOre(this.getUnlocalizedName().replace("tile.", ""), this);

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (entity instanceof IBlockActivated && ((IBlockActivated) entity).onActivated(entityPlayer))
        {
            return true;
        }
        return super.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

}
