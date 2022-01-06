package com.windanesz.spellbundle.integration.treasure2.ice_chest;

import com.someguyssoftware.treasure2.inventory.AbstractChestContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 *
 * @author Mark Gottschling on Jan 16, 2018
 */
public class IceChestContainer extends AbstractChestContainer {
	/**
	 * @param invPlayer
	 * @param inventory
	 */
	public IceChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);

		final int invSize = inventory.getSizeInventory();
		// TODO: alternative idea: use square root? e.g. sq(25) -> 5col * 5row - allows better scaling rather than a constant 9 col count
		// row count can vary based on the strength of the spell summoning the chest, but the colum count is always 9
		int columnCount = 0;
		int rowCount = 0;

		// TODO: this is definitely fcked up
		switch (invSize) {
			case 9:
				// has 6 less columns - to center move the xpos over by xspacing*3
				setContainerInventoryXPos(8 + getSlotXSpacing() * 3);
				columnCount = 3;
				rowCount = 3;
				break;
			case 15:
				// has 4 less columns - to center move the xpos over by xspacing*2
				setContainerInventoryXPos(8 + getSlotXSpacing() * 2);
				columnCount = 5;
				rowCount = 3;
				break;
			case 21:
				// armoire has 2 less columns - to center move the xpos over by xspacing*2
				setContainerInventoryXPos(8 + getSlotXSpacing());
				columnCount = 7;
				rowCount = 3;
				break;
			case 27:
				// armoire has 2 less columns - to center move the xpos over by xspacing*2
				columnCount = 9;
				rowCount = 3;
				setContainerInventoryXPos(8);
				break;
			case 36:
				// armoire has 2 less columns - to center move the xpos over by xspacing*2
				columnCount = 9;
				rowCount = 4;
				setPlayerInventoryYPos(84 + getSlotYSpacing());
				setHotbarYPos(142 + getSlotYSpacing());
				setContainerInventoryXPos(8);
				break;
			case 45:
				// armoire has 2 less columns - to center move the xpos over by xspacing*2
				columnCount = 9;
				rowCount = 5;
				setPlayerInventoryYPos(84 + getSlotYSpacing() * 2);
				setHotbarYPos(142 + getSlotYSpacing() * 2);
				setContainerInventoryXPos(8);
				break;
			case 54:
			default:
				// armoire has 2 less columns - to center move the xpos over by xspacing*2
				columnCount = 9;
				rowCount = 6;
				setPlayerInventoryYPos(84 + getSlotYSpacing() * 3);
				setHotbarYPos(142 + getSlotYSpacing() * 3);
				setContainerInventoryXPos(8);
				break;
		}


		setContainerInventoryColumnCount(columnCount);
		setContainerInventoryRowCount(rowCount);
		// build the container
		buildContainer(invPlayer, inventory);
	}
}
