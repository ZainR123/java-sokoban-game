package com.model.engine;

/**
 * Represents the objects in the game
 * Each object assigned a symbol which is used to read maps
 *
 * @author Zain Rashid-modified
 */
public enum GameObject {
    /**
     * Code symbol for a wall
     */
    WALL('W'),
    /**
     * Code symbol for a white space
     */
    EMPTY('W'),
    /**
     * Code symbol for the floor
     */
    FLOOR(' '),
    /**
     * Code symbol for a crate
     */
    CRATE('C'),
    /**
     * Code symbol for a diamond
     */
    DIAMOND('D'),
    /**
     * Code symbol for a keeper
     */
    KEEPER('S'),
    /**
     * Code symbol for a crate on diamond
     */
    CRATE_ON_DIAMOND('O'),
    /**
     * Code symbol for a debug object
     */
    DEBUG_OBJECT('=');

    private final char m_symbol;

    /**
     * GameObject constructor gets current instances of certain objects
     *
     * @param symbol code for corresponding game object
     */
    GameObject(final char symbol) {
        this.m_symbol = symbol;
    }

    /**
     * Returns the enum associated with a char
     * If the char is not associated with any enum, it will return WALL as default value
     *
     * @param c - the char to look for
     * @return the game object corresponding to the char
     */
    public static GameObject fromChar(char c) {
        for (GameObject t : GameObject.values()) {
            if (Character.toUpperCase(c) == t.m_symbol) {
                return t;
            }
        }

        return WALL;
    }

    /**
     * Returns the string representation of the symbol.
     *
     * @return String
     * @deprecated
     */
    @Deprecated
    public String getStringSymbol() {
        return String.valueOf(m_symbol);
    }

    /**
     * Returns the symbol associated with the game object.
     *
     * @return the symbol associated with the game object.
     */
    public char getCharSymbol() {
        return m_symbol;
    }
}