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

//TODO Allow for custom ship and attack loading

public class ShipCombat implements Serializable
{
    public static void main(String[] args) throws IOException {
        //Variable declarations
        boolean exit = false; //Boolean to track whether or not to exit the main program loop
        Scanner scanner = new Scanner(System.in); //Scanner to read in user input
        Random rand = new Random();
        String command; //Command to be executed (as input by user)
        ArrayList<Ship> shipList = new ArrayList<Ship>();
        ArrayList<Attack> attackList = new ArrayList<Attack>();

        //TODO Initialize attackList and presetList here!

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

        System.out.println("GSNCS Version 1.2.0");
        System.out.println("\nEnter a command (or HELP to view commands): ");

        while(!exit)
        {
            System.out.print(">");
            command = scanner.nextLine().toUpperCase(); //Get the user's input (in all caps)

            switch (command)
            {
                case "HELP":
                case "?":
                    System.out.println("~~~Help Menu~~~\n" +
                            "ADD (+): Add either a custom or preset ship to the battle\n" +
                            "ATTACK (A): Compute an attack from one ship to another\n" +
                            "DAMAGE: Directly apply damage (or healing) to a ship\n" +
                            "DISPLAY (D): Display statistics of all ships in battle\n" +
                            "EDIT (E): Edit the available attacks and ships, or create more! <Unfinished>\n" +
                            "EXIT (X): Exit the program\n" +
                            "HELP (?): Open the help menu, displaying all possible commands\n" +
                            "LOAD (L): Load ship data from a save file\n" +
                            "REMOVE (R): Open the Remove Ship menu\n" +
                            "SAVE (S): Create a save file containing current ship data\n");
                    break;

                case "ADD":
                case "+":
                    add(shipList);
                    break;

                case "ATTACK":
                case "A":
                    attack(shipList);
                    break;

                case "DAMAGE":
                    damage(shipList);
                    break;

                case "DISPLAY":
                case "D":
                    display(shipList);
                    break;

                case "EDIT":
                case "E":
                    edit(attackList);
                    break;

                case "EXIT":
                case "X":
                    exit = true;
                    break;

                case "LOAD":
                case "L":
                    load(shipList);
                    break;

                case "REMOVE":
                case "R":
                    remove(shipList);
                    break;

                case "SAVE":
                case "S":
                    save(shipList);
                    break;

                default:
                    //System.out.println("ERROR: Invalid Command! To view all valid commands and syntax, enter HELP");
                    break;
            }
            //System.out.println("");
        }
    }

    //Calculate whether or not an attack will hit
    public static void rollAttack(Attack attack, Ship defender, int advantageState)
    {
        Random rand = new Random();
        int toHit;
        int d20 = rand.nextInt(20) + 1; //Get an int from 1-20

        //Disadvantage
        if(advantageState == 0)
        {
            //Local Variable Declaration
            int roll1;
            int roll2;

            //Roll Attack
            roll1 = d20;
            d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll2 = d20;

            //Use worse roll of the two for calculations (disadvantage)
            if(roll1 > roll2)
                toHit = roll2;
            else
                toHit = roll1;
        }
        //Advantage
        else if(advantageState == 2)
        {
            //Local variable declaration
            int roll1;
            int roll2;

            //Roll attack
            roll1 = d20;
            d20 = rand.nextInt(20) + 1; //Get an int from 1-20
            roll2 = d20;

            //Use higher of the two rolls for calculations (advantage)
            if(roll1 > roll2)
                toHit = roll1;
            else
                toHit = roll2;
        }
        //Normal Attack
        else
            toHit = d20;

        //Roll attack, and apply relevant damage
        if(!attack.targetSails)
        {
            //Critical Hit
            if(toHit == 20)
            {
                System.out.println("Attacker rolled a " + (toHit + attack.attackMod) + ", hitting " + defender.name);
                calculateDamage(defender, attack, true);
            }
            //Normal Hit
            else if(toHit + attack.attackMod >= defender.hullAC)
            {
                System.out.println("Attacker rolled a " + (toHit + attack.attackMod) + ", hitting " + defender.name);
                calculateDamage(defender, attack, false);
            }
            else
                System.out.println("Attacker rolled a " + (toHit + attack.attackMod) + ", missing " + defender.name);
        }
    }

    //Calculates the damage dealt by an attack, based on number of dice rolled, type of dice rolled, and damage modifiers
    public static void calculateDamage(Ship defender, Attack attack, boolean isCrit)
    {
        Random rand = new Random();
        String critString = "";
        int currentDice = attack.numDice;
        int sumDamage = 0; //Total damage dealt

        //Roll double dice on critical
        if(isCrit)
        {
            currentDice = currentDice * 2;
            critString = "CRITICAL HIT! ";
        }

        for(int i=0; i<currentDice;i++)
        {
            sumDamage += (rand.nextInt(attack.numFaces) + 1); //Add 1dN roll to the total damage
        }

        //Remove damage equal to Damage Threshold from total damage
        sumDamage -= defender.damageThreshold;

        //If there is a damage modifier (e.g +5), add this here
        sumDamage += attack.damageMod;

        //Prevent negative damage rolls from occurring
        if(sumDamage < 0)
            sumDamage = 0;

        //Sails Attack
        if(attack.targetSails)
        {
            defender.sailsHP -= sumDamage;
            System.out.println(critString + attack.attackerName + " hit " + defender.name + "'s sails for " + sumDamage + "damage! "
                    + defender.name + "'s sails have " + defender.sailsHP + " HP remaining!");
        }
        //Hull Attack
        else
        {
            defender.hullHP -= sumDamage;
            System.out.println(critString + attack.attackerName + " hit " + defender.name + "'s hull for " + sumDamage + "damage! "
                    + defender.name + "'s hull has " + defender.sailsHP + " HP remaining!");
        }
    }

    public static void display(ArrayList<Ship> shipList)
    {
        ArrayList<Ship> enemyList = new ArrayList<Ship>();
        int allyHP = 0;
        int enemyHP = 0;
        boolean noAllies = false;
        boolean noEnemies = false;
        double tideOfWar = 0;
        boolean isEmpty = false;

        for(int i=0; i<shipList.size(); i++)
        {
            //Add ally HP to the total (Used for tide of war meter)
            if(shipList.get(i).isAlly)
            {
                allyHP += shipList.get(i).hullHP;
                allyHP += shipList.get(i).sailsHP;
            }
            //Add enemy HP to the total (Used for tide of war meter)
            else
            {
                enemyHP += shipList.get(i).hullHP;
                enemyHP += shipList.get(i).sailsHP;
            }
        }

        //Prevent Divide by Zero errors
        if(allyHP <= 0)
            noAllies = true;
        if(enemyHP <= 0)
            noEnemies = true;

        //Nothing in list
        if(noAllies && noEnemies)
        {
            isEmpty = true;

        }
        //Enemy ships in list only
        else if(noAllies)
        {
            System.out.println("                                                Tide of War:");
            System.out.println("ALLIES ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ ENEMIES");
        }
        //Allied ships in list only
        else if(noEnemies)
        {
            System.out.println("                                                Tide of War:");
            System.out.println("ALLIES ████████████████████████████████████████████████████████████████████████████████ ENEMIES");
        }
        //Allied and enemy ships in list
        else
        {
            //Calculate "Tide of War"
            tideOfWar = (double)allyHP / (allyHP + enemyHP);

            //Print out "Tide of War" bar
            System.out.println("                                           Tide of War:");
            System.out.print("           ALLIES  ");
            for(int i=0; i < tideOfWar * 60; i++)
                System.out.print("█");
            for(int i=0; i < (60 - tideOfWar * 60); i++)
                System.out.print("░");
            System.out.println("  ENEMIES");
        }


        if(!isEmpty)
        {
            //Print out allied ship heading
            System.out.println("                                            ~~ALLIES~~");
            System.out.println("        Name                           Hull                            Sails         | Crew");

            //Print all data for allied ships, and put enemy ships in their own arraylist to be output later
            for (int i = 0; i < shipList.size(); i++) {
                if (shipList.get(i).isAlly)
                    System.out.println(shipList.get(i).getStatus());
                else
                    enemyList.add(shipList.get(i));
            }

            //Print out enemy ship heading
            System.out.println("\n                                            ~~ENEMIES~~");
            System.out.println("        Name                           Hull                            Sails         | Crew");

            for (int i = 0; i < enemyList.size(); i++)
                System.out.println(enemyList.get(i).getStatus());
        }
        else
        {
            System.out.println("  ______                 _         _ \n" +
                    " |  ____|               | |       | |\n" +
                    " | |__   _ __ ___  _ __ | |_ _   _| |\n" +
                    " |  __| | '_ ` _ \\| '_ \\| __| | | | |\n" +
                    " | |____| | | | | | |_) | |_| |_| |_|\n" +
                    " |______|_| |_| |_| .__/ \\__|\\__, (_)\n" +
                    "                  | |         __/ |  \n" +
                    "                  |_|        |___/   ");
        }
    }


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

        if(shipList.size() <= 0)
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


        //Create Attack Object
        Attack attack = new Attack();

        if(attackName.equalsIgnoreCase("9-Pound Cannon"))
        {
            attack.name = attackName;
            attack.attackerName = shipList.get(attacker).name;
            attack.numDice = 4;
            attack.numFaces = 10;
            attack.attackMod = 6;
            attack.damageMod = 0;
            attack.targetSails = false;
        }

        else if(attackName.equalsIgnoreCase("12-Pound Cannon"))
        {
            attack.name = attackName;
            attack.attackerName = shipList.get(attacker).name;
            attack.numDice = 6;
            attack.numFaces = 10;
            attack.attackMod = 6;
            attack.damageMod = 0;
            attack.targetSails = false;
        }

        else if(attackName.equalsIgnoreCase("18-Pound Cannon"))
        {
            attack.name = attackName;
            attack.attackerName = shipList.get(attacker).name;
            attack.numDice = 8;
            attack.numFaces = 10;
            attack.attackMod = 6;
            attack.damageMod = 0;
            attack.targetSails = false;
        }


        //If 'BACK' has not been selected, continue with the attack!
        if(!backSelected)
        {
            System.out.println(attack.name + " selected. How many to fire?");
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

            //Roll each attack
            for(int i=0; i < fireCount; i++)
                rollAttack(attack, shipList.get(defender), advantage);

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
        Scanner scanner = new Scanner(System.in); //Scanner scanner is a scanner!
        boolean exitLoop;

        if(shipList.size() == 0)
            System.out.println("ERROR: No ships to remove! Returning to Main Menu");
        else
        {
            exitLoop = false;

            //Loop until ship is removed or user exits menu
            while(!exitLoop)
            {
                boolean invalidInput = false;
                //Print list of ships to screen
                for(int i=0; i<shipList.size();i++)
                    System.out.println((i+1) + ": " + shipList.get(i).name);
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
                //Remove ship from list
                else
                {
                    System.out.println(shipList.get(Integer.parseInt(removeInput)-1).name + " has been removed!");
                    shipList.remove(Integer.parseInt(removeInput)-1);
                }
            }
        }
    }


    public static void save(ArrayList<Ship> shipList) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        String currentDir = System.getProperty("user.dir");
        String saveDir = currentDir + "Data\\Saves";

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
        File saveDir = new File(currentDir + "Data\\Saves");
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

    public static void edit(ArrayList<Attack> attackList)
    {
        boolean exitLoop = false;
        String editInput;
        Scanner scanner = new Scanner(System.in);
        while(!exitLoop)
        {
            System.out.println("What would you like to edit? (Enter BACK to exit this menu)\n" +
                    "1). Ship Presets" +
                    "2). Attack Presets");
            editInput = scanner.nextLine();

            //BACK
            if (editInput.equalsIgnoreCase("BACK"))
            {
                return;
            }

            //Ship Presets
            if (editInput.equalsIgnoreCase("1"))
            {

            }

            //Attack Presets
            if (editInput.equalsIgnoreCase("2"))
            {
                boolean attackLoop = true;
                boolean validInput = true;
                while(attackLoop)
                {
                    System.out.println("Which attack preset would you like to edit?\n" +
                            "0: New Preset");
                    for (int i = 0; i < attackList.size(); i++)
                        System.out.println((i + 1) + ": " + attackList.get(i).name);

                    String attackInput = scanner.nextLine();
                    int attackNum = -1;
                    try
                    {
                        attackNum = Integer.parseInt(attackInput);
                    }
                    catch (Exception e)
                    {
                        validInput = false;
                    }

                    if(!validInput)
                    {
                        System.out.println("Invalid Input!\n");
                    }

                    //New Preset
                    else if(attackNum == 0)
                    {

                    }

                    //Existing Preset
                    else if(attackNum > 0 && attackNum < attackList.size())
                    {

                    }

                    else
                    {
                        System.out.println("Invalid Input!");
                    }

                }
            }
        }
    }

}