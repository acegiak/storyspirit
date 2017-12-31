package net.machinespirit.storyspirit;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class Opinion implements MetadataValue {

    private float value = 0.5f;

	@Override
	public boolean asBoolean() {
		return this.value>0;
	}

	@Override
	public byte asByte() {
		return (byte)this.value;
	}

	@Override
	public double asDouble() {
		return this.value;
	}

	@Override
	public float asFloat() {
		return this.value;
	}

	@Override
	public int asInt() {
		return (int)(Math.round(this.value));
	}

	@Override
	public long asLong() {
		return 0;
	}

	@Override
	public short asShort() {
		return 0;
	}

	@Override
	public String asString() {
		return null;
	}

	@Override
	public Plugin getOwningPlugin() {
		return StorySpirit.current;
	}

	@Override
	public void invalidate() {
		
	}

	@Override
	public Object value() {
		return new Float(this.value);
	}

    public void setValue(float v){
        this.value = v;
    }

    public Opinion(float v){
        this.value = v;
    }
}