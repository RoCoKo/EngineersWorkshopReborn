package engineers.workshop.client.container.slot.alloying;

import engineers.workshop.client.page.Page;
import engineers.workshop.common.unit.Unit;
import engineers.workshop.common.items.Upgrade;
import engineers.workshop.common.table.TileTable;
import net.minecraft.item.ItemStack;

public class SlotUnitAlloyQueue extends SlotUnitAlloyInput {

	private int queueId;

	public SlotUnitAlloyQueue(TileTable table, Page page, int id, int x, int y, Unit unit, int queueId) {
		super(table, page, id, x, y, unit);
		this.queueId = queueId;
	}

	@Override
	public boolean isVisible() {
		return isUsed() && super.isVisible();
	}

	@Override
	public boolean isEnabled() {
		return isUsed() && super.isEnabled();
	}

	private boolean isUsed() {
		return queueId < table.getUpgradePage().getUpgradeCount(unit.getId(), Upgrade.QUEUE);
	}

	@Override
	public boolean canShiftClickInto(ItemStack item) {
		return true;
	}
}
