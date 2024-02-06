package com.windanesz.spellbundle.integration.biomesoplenty.common;

import biomesoplenty.common.entities.EntityWasp;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.entity.living.ISummonedCreature;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.util.AllyDesignationSystem;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EntityWaspMinion extends EntityWasp implements ISummonedCreature {

	private int lifetime = -1;
	private UUID casterUUID;

	public int getLifetime() {
		return this.lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public UUID getOwnerId() {
		return this.casterUUID;
	}

	public void onSpawn() {
		this.spawnParticleEffect();
		if (this.getCaster() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer) this.getCaster(), WizardryItems.charm_undead_helmets)) {
			this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
		}

	}

	public void onDespawn() {
		this.spawnParticleEffect();
	}

	private void spawnParticleEffect() {
		if (this.world.isRemote) {
			for (int i = 0; i < 15; ++i) {
				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (double) this.rand.nextFloat() - 0.5, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) this.rand.nextFloat() - 0.5, 0.0, 0.0, 0.0, new int[0]);
			}
		}

	}

	@Override
	public boolean hasParticleEffect() {
		return true;
	}

	public void setOwnerId(UUID uuid) {
		this.casterUUID = uuid;
	}

	public EntityWaspMinion(World worldIn) {
		super(worldIn);
		this.experienceValue = 0;
	}

	protected void initEntityAI() {
		super.initEntityAI();
		targetTasks.taskEntries.clear();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.ticksExisted % 20 == 0) {
			if (this.getAttackTarget() == null || !(this).getAttackTarget().isEntityAlive()) {
				processTargeting(world, this, null);
			}
		}
		this.updateDelegate();
	}

	public static boolean findTarget(EntityLiving target, EntityLivingBase caster, World world) {
		List<EntityLivingBase> possibleTargets = EntityUtils.getLivingWithinRadius(target.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue(), target.posX, target.posY, target.posZ, world);
		possibleTargets.remove(target);
		possibleTargets.remove(target.getRidingEntity());
		possibleTargets.removeIf((e) -> {
			return e instanceof EntityArmorStand;
		});
		EntityLivingBase newAITarget = null;
		Iterator var5 = possibleTargets.iterator();

		while (true) {
			EntityLivingBase possibleTarget;
			do {
				do {
					if (!var5.hasNext()) {
						if (newAITarget != null) {
							target.setAttackTarget(newAITarget);
							return true;
						}

						return false;
					}

					possibleTarget = (EntityLivingBase) var5.next();
				} while (!AllyDesignationSystem.isValidTarget(caster, possibleTarget));
			} while (newAITarget != null && !(target.getDistance(possibleTarget) < target.getDistance(newAITarget)));

			newAITarget = possibleTarget;
		}
	}

	private static void processTargeting(World world, EntityWaspMinion entity, EntityLivingBase currentTarget) {
		EntityLivingBase caster = entity.getCaster();
		if (caster != null) {
			if (AllyDesignationSystem.isValidTarget(caster, currentTarget)) {
				return;
			}

			if (findTarget(entity, caster, world)) {
				return;
			}
		}

		entity.setAttackTarget(null);

	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		this.writeNBTDelegate(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.readNBTDelegate(nbttagcompound);
	}


	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	protected boolean canDropLoot() {
		return false;
	}

	protected Item getDropItem() {
		return null;
	}

	protected ResourceLocation getLootTable() {
		return null;
	}

	public boolean canPickUpLoot() {
		return false;
	}

	protected boolean canDespawn() {
		return this.getCaster() == null && this.getOwnerId() == null;
	}

	public boolean getCanSpawnHere() {
		return this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	public boolean canAttackClass(Class<? extends EntityLivingBase> entityType) {
		return !EntityFlying.class.isAssignableFrom(entityType) || this.getHeldItemMainhand().getItem() instanceof ItemBow;
	}

	public ITextComponent getDisplayName() {
		return (ITextComponent)(this.getCaster() != null ? new TextComponentTranslation("entity.ebwizardry:summonedcreature.nameplate", new Object[]{this.getCaster().getName(), new TextComponentTranslation("entity." + this.getEntityString() + ".name", new Object[0])}) : super.getDisplayName());
	}

	public boolean hasCustomName() {
		return Wizardry.settings.summonedCreatureNames && this.getCaster() != null;
	}

}
