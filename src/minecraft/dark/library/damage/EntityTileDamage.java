package dark.library.damage;

import universalelectricity.core.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Entity designed to take damage and apply it to the tile from an Entity. Simulates the tile is
 * alive and can be harmed by normal AIs without additional code.
 * 
 * @author DarkGuardsman
 * 
 */
public class EntityTileDamage extends Entity implements IEntityAdditionalSpawnData
{

	private TileEntity host;
	int hp = 100;

	public EntityTileDamage(World par1World)
	{
		super(par1World);
		this.isImmuneToFire = true;
		this.setSize(1.1F, 1.1F);
	}

	public EntityTileDamage(TileEntity c)
	{
		this(c.worldObj);
		this.setPosition(c.xCoord + 0.5, c.yCoord, c.zCoord + 0.5);
		this.host = c;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, int ammount)
	{
		if (this.host instanceof IHpTile)
		{
			return ((IHpTile) this.host).onDamageTaken(source, ammount);
		}
		else
		{
			if ((hp -= ammount) <= 0)
			{
				if (this.host != null)
				{
					Vector3 vec = new Vector3(this.host.xCoord, this.host.yCoord, this.host.zCoord);
					int id = vec.getBlockID(this.worldObj);
					int meta = vec.getBlockID(this.worldObj);
					Block block = Block.blocksList[id];
					if (block != null)
					{
						block.breakBlock(this.worldObj, this.host.xCoord, this.host.yCoord, this.host.zCoord, id, meta);
					}
					vec.setBlock(this.worldObj, 0);
				}
				this.setDead();

			}
			return true;
		}
	}

	@Override
	public String getEntityName()
	{
		return "EntityTileTarget";
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data)
	{
		if (this.host != null)
		{
			data.writeInt(this.host.xCoord);
			data.writeInt(this.host.yCoord);
			data.writeInt(this.host.zCoord);
		}
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data)
	{
		this.host = this.worldObj.getBlockTileEntity(data.readInt(), data.readInt(), data.readInt());
	}

	@Override
	public void onUpdate()
	{
		if (this.host == null || this.host.isInvalid())
		{
			this.setDead();
			return;
		}
		else if (this.host instanceof IHpTile && !((IHpTile) this.host).isAlive())
		{
			this.setDead();
			return;
		}
		else
		{
			this.setPosition(this.host.xCoord + 0.5, this.host.yCoord, this.host.zCoord + 0.5);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		// TODO Auto-generated method stub

	}

	public void moveEntity(double par1, double par3, double par5)
	{

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
	{
		return AxisAlignedBB.getBoundingBox(this.posX - .6, this.posY - .6, this.posZ - .6, this.posX + .6, this.posY + .6, this.posZ + .6);

	}

	@Override
	protected void entityInit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_98034_c(EntityPlayer par1EntityPlayer)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderVec3D(Vec3 par1Vec3)
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double par1)
	{
		return false;
	}

	@Override
	public void setVelocity(double par1, double par3, double par5)
	{

	}

	@Override
	public boolean isInsideOfMaterial(Material par1Material)
	{
		return false;
	}

	@Override
	public boolean interact(EntityPlayer player)
	{
		if (this.host != null && player != null)
		{
			Block block = Block.blocksList[this.worldObj.getBlockId(this.host.xCoord, this.host.yCoord, this.host.zCoord)];
			if (block != null)
			{
				return block.onBlockActivated(this.worldObj, this.host.xCoord, this.host.yCoord, this.host.zCoord, player, 0, 0, 0, 0);
			}
		}
		return false;
	}
}
