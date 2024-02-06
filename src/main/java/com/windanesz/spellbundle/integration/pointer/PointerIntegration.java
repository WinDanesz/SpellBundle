package com.windanesz.spellbundle.integration.pointer;

import com.windanesz.spellbundle.Settings;
import com.windanesz.spellbundle.integration.Integration;
import com.windanesz.spellbundle.registry.SBLoot;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class PointerIntegration extends Integration {

	// singleton instance
	private static final PointerIntegration instance = new PointerIntegration();
	private static final String modId = "pointer";

	private static final List<Spell> SPELL_LIST = new ArrayList<>();
	private static final List<Item> ARTEFACT_LIST = new ArrayList<>();

	private boolean isLoaded;

	private PointerIntegration() {}

	public static Integration getInstance() { return instance; }

	/************* overrides *************/

	@Override
	public String getModid() { return modId; }

	@Override
	public void init() {
		// register compat instance
		Integration.register(getModid(), getInstance());

		isLoaded = Loader.isModLoaded(getModid());

		if (!isEnabled()) { return; }

		// init stuff
		initCustom();

	}

	@Override
	public boolean isEnabled() {
		return Settings.generalSettings.pointer_integration && isLoaded;
	}

	/**
	 * List used to track which spells belong to this supported mod. Used for spell disabling in postInit in {@link Integration#setDisables()}.
	 *
	 * @return The list of this integration's spells.
	 */
	@Override
	public List<Spell> getSpells() {
		return SPELL_LIST;
	}

	/**
	 * Adds a spell to this integration's list of spells.
	 *
	 * @param spell spell to add.
	 * @return the passed in spell for method chaining.
	 */
	@Override
	public Spell addSpell(Spell spell) {
		if (!SPELL_LIST.contains(spell)) {
			SPELL_LIST.add(spell);
		}
		return spell;
	}

	/**
	 * List used for loot injection in {@link SBLoot#onLootTableLoadEvent(net.minecraftforge.event.LootTableLoadEvent)} and to disable the artefact in
	 * {@link Integration#setDisables()} if the supported mod is not present.
	 *
	 * @return list of all registered ItemArtefacts for this supported mod
	 */
	@Override
	public List<Item> getArtefacts() {
		return ARTEFACT_LIST;
	}

	@Override
	public void addArtefact(Item item) {
		ARTEFACT_LIST.add(item);
	}

	private void initCustom() {

	}
}
