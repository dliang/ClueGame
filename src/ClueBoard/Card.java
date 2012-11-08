package ClueBoard;

public class Card {
	private String name;
	public enum CardType {ROOM, WEAPON, PERSON};
	public CardType cardtype;
	
	public Card(String name, CardType type) {
		this.name = name;
		cardtype = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCardName() {
		return name;
	}
	
	
	public boolean equals(Card card) {
		if (name.equals(card.getCardName())){
			return true;
		}
		return false;
	}

	public CardType getCardtype() {
		return cardtype;
	}

	public void setCardtype(CardType cardtype) {
		this.cardtype = cardtype;
	}		
}
