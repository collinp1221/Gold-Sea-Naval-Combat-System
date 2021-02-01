/*
Gold Sea Ship Combat Manager
Written By: Collin Puchta
Rules/Logic Provided By: Summer Yeager, Jack Anson, Collin Puchta
Description: Program to manage naval ship combat encounters for the game Dungeons & Dragons.
*/

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class ShipCombat implements Serializable
{
    public static void main(String[] args) throws IOException {
        //Variable declarations
        boolean exit = false; //Boolean to track whether or not to exit the main program loop
        Scanner scanner = new Scanner(System.in); //Scanner to read in user input
        Random rand = new Random();
        String command; //Command to be executed (as input by user)
        ArrayList<Ship> shipList = new ArrayList<Ship>();

        //Print out sweet looking ASCII logo
        System.out.println("\n   _____       _     _    _____              _   _                  _ \n" +
                "  / ____|     | |   | |  / ____|            | \\ | |                | |\n" +
                " | |  __  ___ | | __| | | (___   ___  __ _  |  \\| | __ ___   ____ _| |\n" +
                " | | |_ |/ _ \\| |/ _` |  \\___ \\ / _ \\/ _` | | . ` |/ _` \\ \\ / / _` | |\n" +
                " | |__| | (_) | | (_| |  ____) |  __/ (_| | | |\\  | (_| |\\ V / (_| | |\n" +
                "  \\_____|\\___/|_|\\__,_| |_____/ \\___|\\__,_| |_| \\_|\\__,_| \\_/ \\__,_|_|\n");

        System.out.println("   _____                _           _      _____ _                 _       _             \n" +
                "  / ____|              | |         | |    / ____(_)               | |     | |            \n" +
                " | |     ___  _ __ ___ | |__   __ _| |_  | (___  _ _ __ ___  _   _| | __ _| |_ ___  _ __ \n" +
                " | |    / _ \\| '_ ` _ \\| '_ \\ / _` | __|  \\___ \\| | '_ ` _ \\| | | | |/ _` | __/ _ \\| '__|\n" +
                " | |___| (_) | | | | | | |_) | (_| | |_   ____) | | | | | | | |_| | | (_| | || (_) | |   \n" +
                "  \\_____\\___/|_| |_| |_|_.__/ \\__,_|\\__| |_____/|_|_| |_| |_|\\__,_|_|\\__,_|\\__\\___/|_|   \n");

        System.out.println("GSNCS Version 1.0.2, programmed by Collin Puchta");

        while(!exit)
        {
            System.out.println("\nEnter a command: ");
            command = scanner.nextLine().toUpperCase(); //Get the user's input (in all caps)

            switch (command)
            {
                case "HELP":
                case "?":
                    System.out.println("~~~Help Menu~~~\n" +
                            "ADD (+): Add either a custom or preset ship to the battle\n" +
                            "ATTACK (A): Compute an attack from one ship to another\n" +
                            "DAMAGE: Apply damage (or healing) to a ship\n" +
                            "DISPLAY (D): Display statistics of all ships in battle\n" +
                            "EXIT (X): Exit the program\n" +
                            "HELP (?): Open the help menu, displaying all possible commands\n" +
                            "LOAD (L): Load ship data from a save file <NOT IMPLEMENTED>\n" +
                            "REMOVE (R): Open the Remove Ship menu\n" +
                            "SAVE (S): Create a save file containing current ship data <NOT IMPLEMENTED>\n");
                    break;


                case "EXIT":
                case "X":
                    exit = true;
                    break;

                case "DAMAGE":
                    damage(shipList);
                    break;

                case "TEST":
                    Ship testShip = new Ship("Goblin Ship", 100, 10, 100, 10, 5, 5, false);
                    Ship testShip2 = new Ship("Golden Thread", 100, 10, 100, 10, 5, 5, true);

                    //System.out.println("        Name                        Hull                             Sails            | Crew");
                    //System.out.println(testShip.getStatus());
                    //System.out.println(testShip2.getStatus());

                    shipList.add(testShip);
                    shipList.add(testShip2);
                    break;

                case "DISPLAY":
                case "D":
                    display(shipList);
                    break;

                case "ADD":
                case "+":
                    add(shipList);
                    break;

                case "ATTACK":
                case "A":
                    attack(shipList);
                    break;

                case "REMOVE":
                case "R":
                    remove(shipList);
                    break;

                case "SAVE":
                case "S":
                    save(shipList);
                    break;

                case "LOAD":
                case "L":
                    load(shipList);
                    break;

                default:
                    System.out.println("ERROR: Invalid Command! To view all valid commands and syntax, enter HELP");
                    break;
            }
        }
    }

    //TODO Make nat 1 and nat 20 a thing

    //Calculate whether or not an attack will hit
    //0 = miss, 1 = hit, 2 = crit
    public static boolean rollAttack(int modifier, Ship defender, int advantageState, boolean targetSails)
    {
        Random rand = new Random();
        //Disadvantage
        if(advantageState == 0)
        {
            //Local Variable Declaration
            int roll1;
            int roll2;
            int toHit;

            //Roll Attack
            int d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll1 = d20 + modifier;
            d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll2 = d20 + modifier;

            //Use worse roll of the two for calculations (disadvantage)
            if(roll1 > roll2)
                toHit = roll2;
            else
                toHit = roll1;

            //Target either sail or hull AC (depending on attack type)
            if(!targetSails)
            {
                //Check whether or not the attack will hit
                if(toHit >= defender.hullAC)
                {
                    System.out.println("Attacker rolled a " + toHit + " (at disadvantage), hitting " + defender.name + "'s hull!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + " (at disadvantage), missing " + defender.name + "'s hull!");
                    return false;
                }
            }
            else
            {
                //Check whether or not the attack will hit
                if(toHit >= defender.sailsAC)
                {
                    System.out.println("Attacker rolled a " + toHit + " (at disadvantage), hitting " + defender.name + "'s sails!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + " (at disadvantage), missing " + defender.name + "'s sails!");
                    return false;
                }
            }
        }
        //Advantage
        else if(advantageState == 2)
        {
            //Local variable declaration
            int roll1;
            int roll2;
            int toHit;

            //Roll attack
            int d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll1 = d20 + modifier;
            d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll2 = d20 + modifier;

            //Use higher of the two rolls for calculations (advantage)
            if(roll1 > roll2)
                toHit = roll1;
            else
                toHit = roll2;

            //Target either sail or hull AC (depending on attack type)
            //HULL
            if(!targetSails)
            {
                //Check whether or not the attack will hit
                if(toHit >= defender.hullAC)
                {
                    System.out.println("Attacker rolled a " + toHit + " (at advantage), hitting " + defender.name + "'s hull!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + " (at advantage), missing " + defender.name + "'s hull!");
                    return false;
                }
            }
            //SAILS
            else
            {
                //Check whether or not the attack will hit
                if(toHit >= defender.sailsAC)
                {
                    System.out.println("Attacker rolled a " + toHit + " (at advantage), hitting " + defender.name + "'s sails!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + " (at advantage), missing " + defender.name + "'s sails!");
                    return false;
                }
            }
        }
        //Normal Attack
        else
        {
            int d20 = rand.nextInt(20) + 1; //Get an int from 1-20

            int toHit = d20 + 6;
            //Target either sail or hull AC (depending on attack type)
            if(!targetSails) //If hull is targeted, use that AC
            {
                if(toHit >= defender.hullAC)
                {
                    System.out.println("Attacker rolled a " + toHit + ", hitting " + defender.name + "'s hull!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + ", missing " + defender.name + "'s hull!");
                    return false;
                }

            }

            else //If sails are targeted, use that AC
            {
                if(toHit >= defender.sailsAC)
                {
                    System.out.println("Attacker rolled a " + toHit + ", hitting " + defender.name + "'s sails!");
                    return true;
                }
                else
                {
                    System.out.println("Attacker rolled a " + toHit + ", missing " + defender.name + "'s sails!");
                    return false;
                }
            }
        }
    }

    //Calculates the damage dealt by an attack, based on number of dice rolled, type of dice rolled, and damage modifiers
    public static int calculateDamage(Ship defender, int numDice, int numFaces, int damageModifier, boolean critState)
    {
        Random rand = new Random();
        int sumDamage = 0; //Total damage dealt

        //Roll double dice on critical
        if(critState)
            numDice = numDice * 2;

        for(int i=0; i<numDice;i++)
        {
            sumDamage += (rand.nextInt(numFaces) + 1); //Add 1dN roll to the total damage
        }

        //Remove damage equal to Damage Threshold from total damage
        sumDamage -= defender.damageThreshold;

        //If there is a damage modifier (e.g +5), add this here
        sumDamage += damageModifier;

        //Prevent negative damage rolls from occurring
        if(sumDamage < 0)
            sumDamage = 0;

        //Return the amount of damage dealt
        return sumDamage;
    }

    public static void display(ArrayList<Ship> shipList)
    {
        //TODO Implement "Tide of War" meter that tracks who is winning the battle
        ArrayList<Ship> enemyList = new ArrayList<Ship>();

        //Print out allied ship heading
        System.out.println("                                                 ~~ALLIES~~");
        System.out.println("        Name                        Hull                             Sails           | Crew");

        //Print all data for allied ships, and put enemy ships in their own arraylist to be output later
        for (int i = 0; i < shipList.size(); i++) {
            if (shipList.get(i).isAlly)
                System.out.println(shipList.get(i).getStatus());
            else
                enemyList.add(shipList.get(i));
        }

        //Print out enemy ship heading
        System.out.println("\n                                                 ~~ENEMIES~~");
        System.out.println("        Name                        Hull                             Sails           | Crew");

        for (int i = 0; i < enemyList.size(); i++)
            System.out.println(enemyList.get(i).getStatus());
    }


    //TODO Fix damage threshold for custom ship
    public static void add(ArrayList<Ship> shipList)
    {
        Scanner scanner = new Scanner(System.in);
        boolean exitLoop = false;
        String addInput;
        while (!exitLoop)
        {
            System.out.println("Select one of the following ships to add (by number), or enter 'BACK' to go back to the main menu: \n" +
                    "1) Custom Ship\n" +
                    "2) Warship <Allied>\n" +
                    "3) Sailing Ship <Allied>\n" +
                    "4) Warship <Enemy>\n" +
                    "5) Sailing Ship <Enemy>");
            addInput = scanner.nextLine();
            //Custom Ship Creation
            if (addInput.equalsIgnoreCase("1"))
            {
                String allyInput = "";
                Ship customShip = new Ship();
                customShip.initialize();
                if (customShip.crew == 10000)
                    System.out.println("Ship creation has been cancelled");
                else
                    shipList.add(customShip);
            }
            else if (addInput.equalsIgnoreCase("2"))
            {
                System.out.println("Enter the name of this ship, or press ENTER for a generic name");
                String shipName = scanner.nextLine();
                if(shipName.equalsIgnoreCase(""))
                {
                    shipName = "Allied Warship";
                }
                System.out.println("Allied Warship named " + shipName + " has been added!");
                Ship newShip = new Ship(shipName, 400, 15, 100, 15, 60, 20, true);
                //allyShip.hullHP -= rand.nextInt(75);
                //allyShip.sailsHP -= rand.nextInt(25);
                shipList.add(newShip);
                exitLoop = true;
            }
            else if (addInput.equalsIgnoreCase("3"))
            {
                System.out.println("Enter the name of this ship, or press ENTER for a generic name");
                String shipName = scanner.nextLine();
                if(shipName.equalsIgnoreCase(""))
                {
                    shipName = "Allied Sailing Ship";
                }
                System.out.println("Allied Sailing Ship named " + shipName + " has been added!");
                Ship newShip = new Ship(shipName, 250, 13, 75, 13, 30, 20, true);
                shipList.add(newShip);
                exitLoop = true;
            }
            else if (addInput.equalsIgnoreCase("4"))
            {
                System.out.println("Enter the name of this ship, or press ENTER for a generic name");
                String shipName = scanner.nextLine();
                if(shipName.equalsIgnoreCase(""))
                {
                    shipName = "Enemy Warship";
                }
                System.out.println("Enemy Warship named " + shipName + " has been added!");
                Ship newShip = new Ship(shipName, 400, 15, 100, 15, 60, 20, false);
                shipList.add(newShip);
                exitLoop = true;
            }
            else if (addInput.equalsIgnoreCase("5"))
            {
                System.out.println("Enter the name of this ship, or press ENTER for a generic name");
                String shipName = scanner.nextLine();
                if(shipName.equalsIgnoreCase(""))
                {
                    shipName = "Enemy Sailing Ship";
                }
                System.out.println("Enemy Sailing Ship named " + shipName + " has been added!");
                Ship newShip = new Ship(shipName, 250, 13, 75, 13, 30, 20, false);
                shipList.add(newShip);
                exitLoop = true;
            }
            else if (addInput.equalsIgnoreCase("6")) {
                System.out.println("ERROR: Invalid selection!");
                exitLoop = true;
            } else if (addInput.equalsIgnoreCase("7")) {
                System.out.println("ERROR: Invalid selection!");
                exitLoop = true;
            }
            //CANCEL input. Exits the ADD menu
            else if (addInput.equalsIgnoreCase("BACK") || addInput.equalsIgnoreCase("B")) {
                exitLoop = true;
            }
            else
            {
                System.out.println("ERROR: Invalid selection!");
            }
        }
    }

    public static void damage(ArrayList<Ship> shipList)
    {
        Scanner scanner = new Scanner(System.in);

        if(shipList.size() <= 0)
            System.out.println("ERROR: Nothing to damage! Returning to Main Menu");
        else
        {
            boolean exitLoop = false;
            for(int i=0; i<shipList.size();i++)
                System.out.println((i+1) + ": " + shipList.get(i).name);

            while(!exitLoop)
            {
                boolean invalidInput = false;
                int target = 0;
                System.out.println("Which ship would you like to damage? (Enter the number, or enter BACK to exit this menu)");
                String damageInput = scanner.nextLine();
                try
                {
                    target = (Integer.parseInt(damageInput) - 1);
                }
                catch (Exception e)
                {
                    invalidInput = true;
                }
                if(damageInput.equalsIgnoreCase("BACK"))
                {
                    exitLoop = true;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid input!");
                }
                else if(target < 0)
                {
                    System.out.println("ERROR: Value cannot be less than 1!");
                }
                else if(target >= shipList.size())
                {
                    System.out.println("ERROR: Invalid option! The number you selected is not present!");
                }
                else
                {
                    boolean damageLoop = true;
                    while(damageLoop)
                    {
                        System.out.println("How much damage to apply to ship? (Enter a negative number to heal):");
                        try
                        {
                            String damageValue = scanner.nextLine();
                            shipList.get(target).hullHP -= Integer.parseInt(damageValue);
                            System.out.println(damageValue + " damage has been applied to " +  shipList.get(target).name + "!");
                            if(shipList.get(target).hullHP <= 0)
                            {
                                System.out.println(shipList.get(target).name + " has been destroyed!");
                                shipList.remove(target);
                            }
                            else if(shipList.get(target).hullHP >=  shipList.get(target).hullMaxHP)
                            {
                                System.out.println(shipList.get(target).name + " has been fully healed!");
                                shipList.get(target).hullHP =  shipList.get(target).hullMaxHP;
                            }
                            damageLoop = false;
                        }
                        catch(Exception e)
                        {
                            System.out.println("ERROR, Invalid number!");
                        }
                    }

                }
            }
        }
    }


    public static void attack(ArrayList<Ship> shipList)
    {
        //Local variable declaration
        Scanner scanner = new Scanner(System.in);
        String attackInput = ""; //Stores the  input of which attack to use
        String attackName = "";
        int fireCount = 1;
        int attacker = 0; //Position of attacking ship
        int defender = 0; //Position of defending ship
        int advantage = 1; //Advantage state. 0 = disadvantage, 1 = neutral, 2 = advantage
        boolean backSelected = false;
        boolean exitLoop = false;

        if(shipList.size() <=0)
        {
            System.out.println("ERROR: Cannot attack with no stored ships! Returning to Main Menu");
            return;
        }

        while(!exitLoop)
        {
            System.out.println("Select an attack to compute, or enter 'BACK' to go back to the main menu:\n" +
                    "~ROUNDSHOT~\n" +
                    "1). 9-Pound Cannon\n" +
                    "2). 12-Pound Cannon\n" +
                    "3). 18-Pound Cannon\n");
            attackInput = scanner.nextLine();
            if(attackInput.equalsIgnoreCase("BACK"))
            {
                backSelected = true;
                exitLoop = true;
            }

            //9 Pound Cannon
            if(attackInput.equalsIgnoreCase("1"))
            {
                attackName = "9-Pound Cannon";
                exitLoop = true;
            }
            //12 Pound Cannon
            else if(attackInput.equalsIgnoreCase("2"))
            {
                attackName = "12-Pound Cannon";
                exitLoop = true;
            }
            //18 Pound Cannon
            else if(attackInput.equalsIgnoreCase("3"))
            {
                attackName = "18-Pound Cannon";
                exitLoop = true;
            }
            else
            {
                System.out.println("ERROR: Invalid selection!");
            }
        }

        //If 'BACK' has not been selected, continue with the attack!
        if(!backSelected)
        {
            System.out.println(attackName + " selected. How many to fire?");
            exitLoop = false;
            //Query for a valid integer until one is entered
            while(!exitLoop)
            {
                String fireCountStr = scanner.nextLine();
                try
                {
                    fireCount = Integer.parseInt(fireCountStr);
                    if(fireCount < 1)
                    {
                        System.out.println("ERROR: Please enter a valid number!");
                    }
                    //Only exit loop when a positive (and valid) number has been entered
                    else
                    {
                        exitLoop = true;
                    }
                }
                catch (Exception e)
                {
                    System.out.println("ERROR: Please enter a valid number!");
                }
            }

            //Get defending ship
            exitLoop = false;
            for(int i=0; i<shipList.size();i++)
                System.out.println((i+1) + ": " + shipList.get(i).name);

            while(!exitLoop)
            {
                boolean invalidInput = false;
                System.out.println("Who will be the target?");
                String defenderInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(attackInput);
                }
                catch (Exception e)
                {
                    invalidInput = true;
                }
                if(invalidInput)
                {
                    System.out.println("ERROR: Invalid input!");
                }
                else if(Integer.parseInt(defenderInput) < 1)
                {
                    System.out.println("ERROR: Value cannot be less than 1!");
                }
                else if(Integer.parseInt(defenderInput) > shipList.size())
                {
                    System.out.println("ERROR: Invalid option! The number you selected is not present!");
                }
                else
                {
                    defender = (Integer.parseInt(defenderInput) - 1);
                    exitLoop = true;
                }
            }

            exitLoop = false;
            while(!exitLoop)
            {
                System.out.println("Attacking at: \n" +
                        "1). Disadvantage\n" +
                        "2). Normal Attack\n" +
                        "3). Advantage");

                String input = scanner.nextLine();

                //Disadvantage
                if(input.equalsIgnoreCase("1"))
                {
                    advantage = 0;
                    exitLoop = true;
                }
                //Normal Attack
                else if(input.equalsIgnoreCase("2"))
                {
                    advantage = 1;
                    exitLoop = true;
                }
                //Advantage
                else if(input.equalsIgnoreCase("3"))
                {
                    advantage = 2;
                    exitLoop = true;
                }
                else
                    System.out.println("Invalid Input!");
            }

            //Loop for each separate attack
            for(int i=0; i < fireCount; i++)
            {
                if(attackName.equalsIgnoreCase("9-Pound Cannon"))
                {
                    boolean attackHit = false; //tracks whether or not an attack has hit
                    boolean critState = false; //Tracks whether or not attack is a crit

                    //Calculate whether or not the attack hit
                    if(rollAttack(6, shipList.get(defender), advantage, false))
                        attackHit = true;

                    if(attackHit)
                    {
                        //Get damage dealt (4d10)
                        int damage = calculateDamage(shipList.get(defender), 4, 10, 0, false);

                        //Apply damage to defending ship
                        shipList.get(defender).hullHP -= damage;
                        if(shipList.get(defender).hullHP < 0)
                        {
                            shipList.get(defender).hullHP = 0;
                            i = fireCount; //If ship is destroyed, stop attacking it
                        }
                        System.out.println(shipList.get(defender).name + " was hit for " + damage + " damage! " + shipList.get(defender).hullHP + " Hull HP remaining!");
                    }
                }


                //Do damage for the appropriate type of attack
                else if(attackName.equalsIgnoreCase("12-Pound Cannon"))
                {
                    boolean attackHit = false; //tracks whether or not an attack has hit
                    boolean critState = false; //Tracks whether or not an attack is a critical hit

                    //Calculate whether or not the attack hit
                    if(rollAttack(6, shipList.get(defender), advantage, false))
                        attackHit = true;

                    if(attackHit)
                    {
                        //Get damage dealt (6d10)
                        int damage = calculateDamage(shipList.get(defender), 6, 10, 0, false);

                        //Apply damage to defending ship
                        shipList.get(defender).hullHP -= damage;
                        if(shipList.get(defender).hullHP < 0)
                        {
                            shipList.get(defender).hullHP = 0;
                            i = fireCount; //If ship is destroyed, stop attacking it
                        }
                        System.out.println(shipList.get(defender).name + " was hit for " + damage + " damage! " + shipList.get(defender).hullHP + " Hull HP remaining!");
                    }
                }


                else if(attackName.equalsIgnoreCase("18-Pound Cannon"))
                {
                    boolean attackHit = false; //tracks whether or not an attack has hit
                    boolean critState = false; //Tracks whether or not an attack is a critical hit

                    //Calculate whether or not the attack hit
                    if(rollAttack(8, shipList.get(defender), advantage, false))
                        attackHit = true;

                    if(attackHit)
                    {
                        //Get damage dealt (8d10)
                        int damage = calculateDamage(shipList.get(defender), 8, 10, 0, false);

                        //Apply damage to defending ship
                        shipList.get(defender).hullHP -= damage;
                        if(shipList.get(defender).hullHP < 0)
                        {
                            shipList.get(defender).hullHP = 0;
                            i = fireCount; //If ship is destroyed, stop attacking it
                        }
                        System.out.println(shipList.get(defender).name + " was hit for " + damage + " damage! " + shipList.get(defender).hullHP + " Hull HP remaining!");
                    }
                }
            }


            //Check if ship has been destroyed
            if(shipList.get(defender).hullHP <= 0)
            {
                System.out.println(shipList.get(defender).name + " was destroyed!");
                shipList.remove(defender);
            }
            else if(shipList.get(defender).crew <= 0)
            {
                System.out.println(shipList.get(defender).name + " had no remaining crew, and sank!");
                shipList.remove(defender);
            }
        }
    }


    public static void remove(ArrayList<Ship> shipList)
    {
        Scanner scanner = new Scanner(System.in);
        boolean exitLoop;



        if(shipList.size() == 0)
            System.out.println("ERROR: No ships to remove! Returning to Main Menu");
        else
        {
            exitLoop = false;
            for(int i=0; i<shipList.size();i++)
                System.out.println((i+1) + ": " + shipList.get(i).name);

            while(!exitLoop)
            {
                boolean invalidInput = false;
                System.out.println("Which ship would you like to remove? (Enter the number, or enter BACK to exit this menu)");
                String removeInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(removeInput);
                }
                catch (Exception e)
                {
                    invalidInput = true;
                }
                if(removeInput.equalsIgnoreCase("BACK"))
                {
                    exitLoop = true;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid input!");
                }
                else if(Integer.parseInt(removeInput) < 1)
                {
                    System.out.println("ERROR: Value cannot be less than 1!");
                }
                else if(Integer.parseInt(removeInput) > shipList.size())
                {
                    System.out.println("ERROR: Invalid option! The number you selected is not present!");
                }
                else
                {
                    System.out.println(shipList.get(Integer.parseInt(removeInput)-1) + " has been removed!");
                    shipList.remove(Integer.parseInt(removeInput)-1);
                }
            }
        }
    }


    public static void save(ArrayList<Ship> shipList) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir");
        String saveDir = currentDir + "\\Saves";

        if(shipList.size() <= 0)
        {
            System.out.println("ERROR: Cannot save a file with no ships! Returning to main menu");
            return;
        }

        System.out.println("Enter Save File Name, or BACK to exit");
        String input = scanner.nextLine();

        //Return to main loop if user entered "Back"
        if(input.equalsIgnoreCase("BACK"))
            return;

        //If "Back" was not entered, continue
        File saveFile = new File(saveDir + "\\" + input + ".txt");
        try
        {
            if (saveFile.createNewFile())
            {
                System.out.println("New save file created: " + input + ".txt");
            }
            else
            {
                System.out.println("File " +  input + ".txt " + "already exists. Overwriting save file");
            }
        }
        catch (IOException e)
        {
            System.out.println("An error occurred. Returning to Main Menu");
            e.printStackTrace();
            return;
        }


        FileWriter fw = new FileWriter(saveDir + "\\" + input + ".txt");

        for(int i=0; i<shipList.size(); i++)
        {
            //Write ship data to current line of text file
            fw.write(shipList.get(i).name + ";");
            fw.write(shipList.get(i).hullMaxHP + ";");
            fw.write(shipList.get(i).hullHP + ";");
            fw.write(shipList.get(i).hullAC + ";");
            fw.write(shipList.get(i).sailsMaxHP + ";");
            fw.write(shipList.get(i).sailsHP + ";");
            fw.write(shipList.get(i).sailsAC + ";");
            fw.write(shipList.get(i).crew + ";");
            fw.write(shipList.get(i).damageThreshold + ";");
            fw.write(shipList.get(i).isAlly + ";");

            //Start a new line
            if(i < (shipList.size() - 1))
                fw.write("\n");
        }

        //fw.write("TEST");
        fw.close();
        System.out.println("Save file " + input + ".txt has been created!");
    }


    public static void load(ArrayList<Ship> shipList)
    {
        Scanner scanner = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir");
        File saveDir = new File(currentDir + "\\Saves");
        String saveFile = "";
        String[] pathNames;
        pathNames = saveDir.list();
        boolean exitLoop = false;

        //If there are no save files to load, exit function
        if (pathNames.length <= 0)
        {
            System.out.println("ERROR: No save files to load! Returning to Main Menu");
            return;
        }

        for (int i = 0; i < pathNames.length; i++)
        {
            System.out.println((i + 1) + "). " + pathNames[i]);
        }

        //Get save file to load
        while(!exitLoop)
        {
            System.out.println("Choose a save file to load, or enter BACK to return to the Main Menu");
            String input = scanner.nextLine();

            if(input.equalsIgnoreCase("BACK"))
            {
                return;
            }
            else
            {
                try
                {
                    int num = Integer.parseInt(input);

                    if(num > pathNames.length || num <= 0)
                    {
                        System.out.println("Invalid Input!");
                    }
                    else
                    {
                        saveFile = currentDir + "\\Saves\\" + pathNames[num - 1];
                        exitLoop = true;
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Invalid Input! Please enter a number!");
                }
            }
        }

        try
        {
            //Load data from save file into new arraylist
            ArrayList<Ship> newShipList = new ArrayList<Ship>();
            File save = new File(saveFile);
            Scanner saveReader = new Scanner(save);

            while(saveReader.hasNextLine())
            {
                String line = saveReader.nextLine(); //Single line of the .txt file
                int targetedAttribute = 0;
                int lastSemicolon = 0;
                String name = "";
                int hullMaxHP = 0;
                int hullHP = 0;
                int hullAC = 0;
                int sailsMaxHP = 0;
                int sailsHP = 0;
                int sailsAC = 0;
                int crew = 0;
                int damageThreshold = 0;
                boolean isAlly = false;

                for(int i=0;i<line.length();i++)
                {

                    if(line.charAt(i) == ';')
                    {
                        targetedAttribute ++;
                        lastSemicolon = i;
                    }
                    //Name [0]
                    else if(targetedAttribute == 0 && line.charAt(i + 1) == ';')
                    {
                        name = line.substring((0), (i + 1));
                    }
                    //Hull Max HP [1]
                    else if(targetedAttribute == 1 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        hullMaxHP = Integer.parseInt(rawData);
                    }
                    //Hull HP [2]
                    else if(targetedAttribute == 2 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        hullHP = Integer.parseInt(rawData);
                    }
                    //Hull AC [3]
                    else if(targetedAttribute == 3 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        hullAC = Integer.parseInt(rawData);
                    }
                    //Sails Max HP [4]
                    else if(targetedAttribute == 4 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        sailsMaxHP = Integer.parseInt(rawData);
                    }
                    //Sails HP [5]
                    else if(targetedAttribute == 5 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        sailsHP = Integer.parseInt(rawData);
                    }
                    //Sails AC [6]
                    else if(targetedAttribute == 6 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        sailsAC = Integer.parseInt(rawData);
                    }
                    //Crew [7]
                    else if(targetedAttribute == 7 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        crew = Integer.parseInt(rawData);
                    }
                    //Damage Threshold [8]
                    else if(targetedAttribute == 8 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        damageThreshold = Integer.parseInt(rawData);
                    }
                    //isAlly [9]
                    else if(targetedAttribute == 9 && line.charAt(i + 1) == ';')
                    {
                        String rawData = line.substring((lastSemicolon + 1), (i + 1));
                        if(rawData.equalsIgnoreCase("TRUE"))
                            isAlly = true;
                        else if(rawData.equalsIgnoreCase("FALSE"))
                            isAlly = false;
                        //If data is neither true or false, throw an exception
                        else
                        {
                            throw new Exception();
                        }
                    }
                }
                //Add new ship with specified data to new list
                Ship newShip = new Ship(name, hullMaxHP, hullAC, sailsMaxHP, sailsAC, crew, damageThreshold, isAlly);
                newShip.hullHP = hullHP;
                newShip.sailsHP = sailsHP;
                newShipList.add(newShip);
            }
            //Clear list of ships, and add all ships from saved data to ship list
            shipList.clear();
            shipList.addAll(newShipList);
            System.out.println("Save file loaded successfully!");
        }
        catch(Exception e)
        {
            System.out.println("ERROR: There was a problem reading this save file! Returning to Main Menu");
        }
    }
}