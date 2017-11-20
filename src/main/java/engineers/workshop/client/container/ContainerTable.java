package engineers.workshop.client.container;

import engineers.workshop.common.table.TileTable;
import engineers.workshop.client.container.slot.SlotBase;
import engineers.workshop.client.container.slot.SlotPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerTable extends ContainerBase {

    private TileTable table;

    public ContainerTable(TileTable table, EntityPlayer player) {
        this.table = table;

        table.getSlots().forEach(this::addSlotToContainer);
        InventoryPlayer inventory = player.inventory;

        for (int y = 0; y < NORMAL_ROWS; y++) {
            for (int x = 0; x < SLOTS_PER_ROW; x++) {
                addSlotToContainer(new SlotPlayer(inventory, table, x + y * SLOTS_PER_ROW + SLOTS_PER_ROW, PLAYER_X + x * SLOT_SIZE, y * SLOT_SIZE + PLAYER_Y));
            }
        }

        for (int x = 0; x < SLOTS_PER_ROW; x++) {
            addSlotToContainer(new SlotPlayer(inventory, table, x, PLAYER_X + x * SLOT_SIZE, PLAYER_HOT_BAR_Y));
        }
    }

    private static final int SLOT_SIZE = 18;
    private static final int SLOTS_PER_ROW = 9;
    private static final int NORMAL_ROWS = 3;
    private static final int PLAYER_X = 48;
    private static final int PLAYER_Y = 174;
    private static final int PLAYER_HOT_BAR_Y = 232;

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return table.isUsableByPlayer(player);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        ItemStack itemstack = ItemStack.EMPTY;
        SlotBase slot = (SlotBase)inventorySlots.get(i);
        if(slot != null && slot.getHasStack() && slot.isVisible()) {
            ItemStack slotItem = slot.getStack();
            itemstack = slotItem.copy();
            if(i < table.getSizeInventory()) {
                if(!mergeItemStack(slotItem, table.getSizeInventory() + 28, table.getSizeInventory() + 36, false)) {
                    if(!mergeItemStack(slotItem, table.getSizeInventory(), table.getSizeInventory() + 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if(!mergeItemStack(slotItem, 0, table.getSizeInventory(), false)){
                return ItemStack.EMPTY;
            }
            if(slotItem.getCount() == 0){
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if(slotItem.getCount() != itemstack.getCount()){
                slot.onTake(player,slotItem);
            } else {
                return ItemStack.EMPTY;
            }
        }
        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack item, int start, int end, boolean invert){
        boolean result = false;
        int id = start;

        if (invert) {
            id = end - 1;
        }

        SlotBase slot;
        ItemStack slotItem;

        if (item.isStackable()) {
            while (item.getCount() > 0 && (!invert && id < end || invert && id >= start)) {
                slot = (SlotBase)this.inventorySlots.get(id);
                if (slot.isVisible() && slot.canShiftClickInto(item)) {
                    slotItem = slot.getStack();

                    if (!slotItem.isEmpty() && slotItem.getCount() > 0 && slotItem.getItem() == item.getItem() && (!item.getHasSubtypes() || item.getItemDamage() == slotItem.getItemDamage()) && ItemStack.areItemStackTagsEqual(item, slotItem)) {
                        int size = slotItem.getCount() + item.getCount();

                        int maxLimit = Math.min(item.getMaxStackSize(), slot.getSlotStackLimit(item));
                        if (size <= maxLimit) {
                            item.setCount(0);
                            slotItem.setCount(size);
                            slot.onSlotChanged();
                            result = true;
                        }else if (slotItem.getCount() < maxLimit) {
                            item.setCount(maxLimit - slotItem.getCount());
                            slotItem.setCount(maxLimit);
                            slot.onSlotChanged();
                            result = true;
                        }
                    }
                }

                if (invert) {
                    --id;
                }else{
                    ++id;
                }
            }
        }

        if (item.getCount() > 0){
            if (invert){
                id = end - 1;
            }else{
                id = start;
            }

            while (!invert && id < end || invert && id >= start){
                slot = (SlotBase)this.inventorySlots.get(id);
                slotItem = slot.getStack();

                if (slot.isVisible() && slot.canShiftClickInto(item)) {
                    if (slotItem.isEmpty() && slot.isItemValid(item)) {
                        int stackLimit = slot.getSlotStackLimit(item);
                        if (stackLimit > 0) {
                            int stackSize = Math.min(stackLimit, item.getCount());

                            ItemStack newItem = item.copy();
                            newItem.setCount(stackSize);
                            item.shrink(stackLimit);
                            slot.putStack(newItem);
                            slot.onSlotChanged();

                            item.setCount(0);
                            result = item.isEmpty();
                            break;
                        }
                    }
                }
                if (invert){
                    --id;
                }else{
                    ++id;
                }
            }
        }
        return result;
    }

    @Override
    protected int getSlotStackLimit(Slot slot, ItemStack item) {
        return ((SlotBase)slot).getSlotStackLimit(item);
    }

    @Override
    public boolean canItemBePickedUpByDoubleClick(ItemStack item, Slot slot) {
        return ((SlotBase)slot).canPickUpOnDoubleClick();
    }

    @Override
    public boolean canDragIntoSlot(Slot slot) {
        return ((SlotBase)slot).canDragIntoSlot();
    }

    public TileTable getTable() {
        return table;
    }
}
