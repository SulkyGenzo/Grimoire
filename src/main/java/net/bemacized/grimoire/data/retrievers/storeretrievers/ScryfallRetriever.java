package net.bemacized.grimoire.data.retrievers.storeretrievers;

import net.bemacized.grimoire.data.models.MtgCard;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ScryfallRetriever extends StoreRetriever {
	@Override
	public String getStoreName() {
		return "Scryfall";
	}

	@Override
	public String getStoreId() {
		return "SCF";
	}

	@Override
	public String[] supportedLanguages() {
		return new String[]{"English"};
	}

	@Override
	public long timeout() {
		return 1000 * 60 * 60 * 6; // 6 hours
	}

	@Override
	protected StoreCardPriceRecord _retrievePrice(MtgCard card) throws StoreAuthException, StoreServerErrorException, UnknownStoreException, StoreSetUnknownException {
		card.updateScryfall();
		return new StoreCardPriceRecord(card.getName(), card.getSet().getCode(), null, System.currentTimeMillis(), getStoreId(), new HashMap<String, String>() {{
			put("MTGO", formatPrice(card.getScryfallCard().getTix()) + " TIX");
			put("EUR", "€" + formatPrice(card.getScryfallCard().getEur()));
			put("USD", "$" + formatPrice(card.getScryfallCard().getUsd()));
		}});
	}

	private String formatPrice(@Nullable String price) {
		if (price == null || price.isEmpty()) price = "0";
		try {
			if (Double.parseDouble(price) <= 0) price = "N/A";
		} catch (Exception e) {
			price = "N/A";
		}
		return price;
	}
}