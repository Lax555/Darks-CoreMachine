package dark.core.common.transmit;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityConductor;

import com.builtbroken.common.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dark.core.client.renders.RenderBlockWire;
import dark.core.common.DMCreativeTab;
import dark.core.common.DarkMain;
import dark.core.prefab.machine.BlockMachine;
import dark.core.registration.ModObjectRegistry.BlockBuildData;

public class BlockWire extends BlockMachine
{
    public static float wireResistance = 0.001f;
    public static float ampMax = 10000f;
    public boolean isWireCollision = true;
    public Vector3 minVector = new Vector3(0.3, 0.3, 0.3);
    public Vector3 maxVector = new Vector3(0.7, 0.7, 0.7);

    @SideOnly(Side.CLIENT)
    public Icon wireIcon;

    public BlockWire()
    {
        super(new BlockBuildData(BlockWire.class, "DMWire", Material.cloth).setCreativeTab(DMCreativeTab.tabIndustrial));
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.1f);
        this.setBlockBounds(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
        Block.setBurnProperties(this.blockID, 30, 60);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.wireIcon = par1IconRegister.registerIcon(DarkMain.getInstance().PREFIX + "CopperWire");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta)
    {
        if (meta == 17)
        {
            return this.blockIcon;
        }
        return Block.blockRedstone.getIcon(side, 0);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return this.zeroRendering ? 0 : -1;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityWire();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        setBlockBoundsBasedOnState(world, x, y, z);
        if (tileEntity instanceof IConductor)
        {
            ((IConductor) tileEntity).refresh();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        setBlockBoundsBasedOnState(world, x, y, z);
        if (tileEntity instanceof IConductor)
        {
            ((IConductor) tileEntity).refresh();
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    /** Returns the bounding box of the wired rectangular prism to render. */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        if (this.isWireCollision)
        {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (tileEntity instanceof TileEntityConductor)
            {
                TileEntity[] connectable = ((TileEntityConductor) tileEntity).getAdjacentConnections();

                if (connectable != null)
                {
                    float minX = (float) this.minVector.x;
                    float minY = (float) this.minVector.y;
                    float minZ = (float) this.minVector.z;
                    float maxX = (float) this.maxVector.x;
                    float maxY = (float) this.maxVector.y;
                    float maxZ = (float) this.maxVector.z;

                    if (connectable[0] != null)
                    {
                        minY = 0.0F;
                    }

                    if (connectable[1] != null)
                    {
                        maxY = 1.0F;
                    }

                    if (connectable[2] != null)
                    {
                        minZ = 0.0F;
                    }

                    if (connectable[3] != null)
                    {
                        maxZ = 1.0F;
                    }

                    if (connectable[4] != null)
                    {
                        minX = 0.0F;
                    }

                    if (connectable[5] != null)
                    {
                        maxX = 1.0F;
                    }

                    this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
                }
            }
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
    {
        if (this.isWireCollision)
        {
            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

            if (tileEntity instanceof TileEntityConductor)
            {
                TileEntity[] connectable = ((TileEntityConductor) tileEntity).getAdjacentConnections();

                this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);

                if (connectable[4] != null)
                {
                    this.setBlockBounds(0, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                if (connectable[5] != null)
                {
                    this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, 1, (float) this.maxVector.y, (float) this.maxVector.z);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                if (connectable[0] != null)
                {
                    this.setBlockBounds((float) this.minVector.x, 0, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                if (connectable[1] != null)
                {
                    this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, 1, (float) this.maxVector.z);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                if (connectable[2] != null)
                {
                    this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, 0, (float) this.maxVector.x, (float) this.maxVector.y, (float) this.maxVector.z);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                if (connectable[3] != null)
                {
                    this.setBlockBounds((float) this.minVector.x, (float) this.minVector.y, (float) this.minVector.z, (float) this.maxVector.x, (float) this.maxVector.y, 1);
                    super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
                }

                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
        else
        {
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        }
    }

    @Override
    public void getTileEntities(int blockID, Set<Pair<String, Class<? extends TileEntity>>> list)
    {
        list.add(new Pair<String, Class<? extends TileEntity>>("DMWireTile", TileEntityWire.class));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getClientTileEntityRenderers(List<Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer>> list)
    {
        if (!this.zeroRendering)
        {
            list.add(new Pair<Class<? extends TileEntity>, TileEntitySpecialRenderer>(TileEntityWire.class, new RenderBlockWire()));
        }
    }

    @Override
    public boolean hasExtraConfigs()
    {
        return true;
    }

    @Override
    public void loadExtraConfigs(Configuration config)
    {
        super.loadExtraConfigs(config);
        BlockWire.wireResistance = config.get("Settings", "miliOhms", 1, "Resistance of the wire in 1/1000 of an ohm").getInt() / 1000;
        BlockWire.ampMax = config.get("Settings", "maxAmps", 10000, "Amp limit of the wire").getInt();

    }

    @Override
    public void loadOreNames()
    {
        // TODO Auto-generated method stub

    }
}
