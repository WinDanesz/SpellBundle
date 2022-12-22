package com.windanesz.spellbundle.spell.qualitytools;

import com.windanesz.spellbundle.SpellBundle;
import com.windanesz.spellbundle.integration.qualitytools.QualityToolsUtils;
import com.windanesz.spellbundle.integration.qualitytools.common.enchantments.EnchantmentMagicallyWeaken;
import com.windanesz.spellbundle.registry.SBEnchantments;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.SpellActions;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.InventoryUtils;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WeakenEquipment extends SpellRay {

	public WeakenEquipment() {
		super(SpellBundle.MODID, "weaken_equipment", SpellActions.POINT_UP, false);
		addProperties(EFFECT_DURATION);
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		if (target instanceof EntityPlayer) {
			EntityPlayer targetPlayer = (EntityPlayer) target;
			// Won't work if the weapon already has the enchantment
			if(WizardData.get(targetPlayer) != null
					&& WizardData.get(targetPlayer).getImbuementDuration(SBEnchantments.magically_weakened) <= 0){

				for(ItemStack stack : InventoryUtils.getPrioritisedHotbarAndOffhand(targetPlayer)){

					if(!EnchantmentHelper.getEnchantments(stack).containsKey(SBEnchantments.magically_weakened)){
						// The enchantment level as determined by the damage multiplier. The + 0.5f is so that
						// weird float processing doesn't incorrectly round it down.
						stack.addEnchantment(SBEnchantments.magically_weakened,	1);
						WizardData.get(targetPlayer).setImbuementDuration(SBEnchantments.magically_weakened,
								(int)(getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));

						if (!world.isRemote) {
							if (QualityToolsUtils.hasQuality(stack)) {
								// saving old quality
								stack.getTagCompound().setTag(EnchantmentMagicallyWeaken.PREVIOUS_QUALITY_TAG, stack.getTagCompound().getCompoundTag(QualityToolsUtils.QUALITY_TAG));
							}
							QualityToolsUtils.generateQualityTag(stack, true, 30, false);
						}

						if(world.isRemote){
							for(int i=0; i<10; i++){
								double x = targetPlayer.posX + world.rand.nextDouble() * 2 - 1;
								double y = targetPlayer.posY + targetPlayer.getEyeHeight() - 0.5 + world.rand.nextDouble();
								double z = targetPlayer.posZ + world.rand.nextDouble() * 2 - 1;
								ParticleBuilder.create(ParticleBuilder.Type.SPARKLE).pos(x, y, z).vel(0, 0.1, 0).clr(0.9f, 0.7f, 1).spawn(world);
							}
						}

						this.playSound(world, targetPlayer, ticksInUse, -1, modifiers);
						return true;

					}
				}
			}
			return false;
		}
		
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit,
			@Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers) {
		return false;
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override) {return true;}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser) {return true;}

}
