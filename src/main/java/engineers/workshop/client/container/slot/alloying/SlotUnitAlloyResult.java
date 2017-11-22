package engineers.workshop.client.container.slot.alloying;

import engineers.workshop.client.container.slot.SlotUnit;
import engineers.workshop.client.page.Page;
import engineers.workshop.client.page.unit.Unit;
import engineers.workshop.common.table.TileTable;
import net.minecraft.item.ItemStack;

public class SlotUnitAlloyResult extends SlotUnit {

	public SlotUnitAlloyResult(TileTable table, Page page, int id, int x, int y, Unit unit) {
		super(table, page, id, x, y, unit);
	}

	@Override
	public boolean isBig() {
		return true;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canSupplyItems() {
		return true;
	}

	@Override
	public boolean canAcceptItems() {
		return false;
	}

}
