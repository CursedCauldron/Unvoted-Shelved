package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USGameEvents;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.tags.GameEventTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class USGameEventTagsProvider extends GameEventTagsProvider {

    public USGameEventTagsProvider(DataGenerator p_176826_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_176826_, UnvotedAndShelved.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(GameEventTags.VIBRATIONS).add(USGameEvents.SPIN_HEAD);
    }
}
