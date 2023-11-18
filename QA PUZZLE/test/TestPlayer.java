package test;

import org.junit.Test;
import player.Player;
import utility.Utility;
import cards.*;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TestPlayer {

//Example test
@Test
public void testTakeDamage(){
    new Utility();
    Player player = new Player(10, Utility.generateCards());
    player.takeDamage(1);
    assertEquals(9, player.getHealth());
}

@Test
public void getInitialNumberOfCards_whenPlayerIsInitiated_playerWillHaveSixInitialCards(){
    //Setup
    new Utility();
    Player player = new Player(10, Utility.generateCards());

    //Execute
    int numberOfCards = player.getInitialNumberOfCards();

    //Assert
    assertEquals(6, numberOfCards);
}

@Test
public void drawCard_whenPlayerDrawsACard_cardsInHandIncreaseAndCardsInDeckDecreaseByOne(){
    //Setup
    new Utility();
    Player player = new Player(10, Utility.generateCards());
    int initialDeckSize = player.getNumberOfCardsInDeck();
    int initialHandSize = player.getNumberOfCardsInHand();

    //Execute
    player.drawCard();

    //Setup
    assertEquals(initialDeckSize - 1, player.getNumberOfCardsInDeck());
    assertEquals(initialHandSize + 1, player.getNumberOfCardsInHand());

}

@Test
public void drawCard_whenPlayerDrawsACard_ifNoCardsInDeckKeepSameHandSize(){
    //Setup
    List<Card> cards = new ArrayList<>();
    Player player = new Player(10, cards);
    int initialHandSize = player.getNumberOfCardsInHand();

    //Execute
    try {
        player.drawCard();
    } catch(Exception ex) {        
        assertFalse("Should not throw exception: " + ex, true);
    }

    //Setup
    assertEquals(0, player.getNumberOfCardsInDeck());
    assertEquals(initialHandSize, player.getNumberOfCardsInHand());
}

@Test
public void playCard_whenPlayerPlaysACard_handSizeIsReducedByOne(){
    //Setup
    new Utility();
    Player player = new Player(10, Utility.generateCards());
    player.drawInitialCards();
    int initialHandSize = player.getNumberOfCardsInHand();
    List<Card> currentHand = player.getHand();
    Card cardToBePlayed = currentHand.get(0);

    //Execute
    player.playCard(cardToBePlayed.getNumber());

    //Assert
    assertEquals(initialHandSize-1, player.getNumberOfCardsInHand());
}

@Test
public void playCard_whenPlayerPlaysACard_ifNoCardOfThatTypeContinue(){
    //Setup
    List<Card> cards = new ArrayList<>();
    BoostAttackCard boostCard = new BoostAttackCard();     //BoostAttackCard has an index of 2
    cards.add(boostCard);
    Player player = new Player(10, cards);

    //Execute
    try{
        player.playCard(1); // Plays an index that doesn't exist in hand
        assertTrue(true);
    } catch(Exception ex) {
        assertFalse("Should not throw exception: " + ex, true);
    }
}

@Test
public void playCard_whenPlayerPlaysBoostCard_damageCounterIsIncreasedByThreeAndPlayerDoesNotAttack() {
    //Setup
    List<Card> cards = new ArrayList<>();
    cards.add(new BoostAttackCard());
    Player player = new Player(10, cards);
    int initialDamage = player.getDamage();
    Card cardToPlay=cards.get(0);

    //Execute
    player.drawCard();
    player.playCard(cardToPlay.getNumber());

    //Assert
    assertTrue("Boost should be 3, now it is: " + (player.getDamage() - initialDamage), player.getDamage() == initialDamage + 3);
    assertEquals(false, player.getAttackingStatus());
    }

 @Test
 public void playCard_whenPlayerPlaysAttackCard_damageCounterIsIncreasedAndPlayerDoesAttacks() {
    //Setup
    List<Card> cards = new ArrayList<>();
    cards.add(new AttackCard(3));
    Player player = new Player(10, cards);
    Card cardToPlay = cards.get(0);

    //Execute
    player.drawCard();
    player.playCard(cardToPlay.getNumber());

    //Setup
    assertTrue("Player's damage should increase after playing an AttackCard", player.getDamage() == cardToPlay.getNumber());
    assertTrue("Attacking status should be true after playing an AttackCard", player.getAttackingStatus());
}

@Test
public void playCard_whenPlayerPlaysProtectCard_removeCardFromHand() {
    //Setup
    List<Card> cards = new ArrayList<>();
    cards.add(new ProtectCard());
    Player player = new Player(10, cards);
    Card cardToPlay = cards.get(0);

    //Execute
    player.drawCard();
    player.playCard(cardToPlay.getNumber()); //Protect card in playCard functions just removes the card from hand, else is done in Game.java

    //Assert
    assertEquals(0, player.getHand().size());
}

@Test
public void shuffleDeck_willRandomlyShuffleDeck_ifDeckToShuffle() {
    //Setup
    new Utility();
    Player player = new Player(10, Utility.generateCards());
    List<Card> deck = new ArrayList<>(player.getDeck());

    //Execute
    player.shuffleDeck();
    List<Card> shuffledDeck = player.getDeck();

    //Assert
    assertFalse(deck.equals(shuffledDeck));
}

@Test
public void shuffleDeck_willPass_ifNoDeckToShuffle() {
    //Setup
    List<Card> emptyDeck = new ArrayList<>();
    Player player = new Player(10, emptyDeck);
    //Execute
    player.shuffleDeck();
    List<Card> shuffledDeck = player.getDeck();

    //Assert
    assertTrue(shuffledDeck.equals(new ArrayList<>()));
}
@Test
public void resetDamage_willTurnDamageTo0_methodIsInvoked() {
    //Setup
    List<Card> cards = new ArrayList<>();
    cards.add(new BoostAttackCard());    
    Player player = new Player(10, cards);
    player.drawCard();

    player.playCard(2);   //Plays boost card to make damage stat bigger
    int boostedDamage = player.getDamage();
    assertFalse(boostedDamage == 0);

    //Execute

    player.resetDamage();
    int resetedDamage = player.getDamage();

    //Assert
    assertNotEquals(boostedDamage, resetedDamage);
}
}
