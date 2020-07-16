package de.exceptionflug.mccommons.inventories.api.item;

public abstract class AbstractCustomItemIDMapping extends ItemIDMapping {

	public AbstractCustomItemIDMapping(final int protocolVersionRangeStart, final int protocolVersionRangeEnd, final int id) {
		super(protocolVersionRangeStart, protocolVersionRangeEnd, id);
	}

	public abstract void apply(final ItemStackWrapper stack, final int protocolVersion);

	public abstract boolean isApplicable(final ItemStackWrapper stack, int version, int id, final int durability);

}
