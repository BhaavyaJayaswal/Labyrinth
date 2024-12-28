/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public enum LevelItem {
    EXIT('.'), WALL('#'), EMPTY(' '), PLAYER('@'), DRAGON('D'), PLAYER_AT_EXIT('*');
    
    /**
    * Constructor to initialize the character representation of the level item.
    *
    * @param rep the character representing the level item.
    */
    LevelItem(char rep) {
        representation = rep;
    }
    public final char representation;
}
