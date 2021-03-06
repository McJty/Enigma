package mcjty.enigma.progress.serializers;

import mcjty.enigma.progress.NBTData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static mcjty.enigma.varia.StringRegister.STRINGS;

public class ItemStackSerializer implements NBTData<Integer, ItemStack> {
    @Override
    public Integer getKey(NBTTagCompound tag) {
        return STRINGS.get(tag.getString("s"));
    }

    @Override
    public ItemStack getValue(NBTTagCompound tag) {
        ItemStack stack = ItemStack.EMPTY;
        if (tag.hasKey("item")) {
            stack = new ItemStack(tag.getCompoundTag("item"));
        }
        return stack;
    }

    @Override
    public void serialize(NBTTagCompound tc, Integer key, ItemStack value) {
        tc.setString("s", STRINGS.get(key));
        if (!value.isEmpty()) {
            NBTTagCompound itemtag = new NBTTagCompound();
            value.writeToNBT(itemtag);
            tc.setTag("item", itemtag);
        }
    }
}
