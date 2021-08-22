import java.util.Scanner;

public class Ship
{
    public int hullMaxHP;
    public int hullHP;
    public int hullAC;
    public int sailsMaxHP;
    public int sailsHP;
    public int sailsAC;
    public int crew;
    public Crew[] crewArray; //Array to store all Crew objects
    public int maneuverability;
    public int damageThreshold;
    public String name; //Ship Name
    public boolean isAlly;

    //Default constructor. Honestly, we should never ever use this, but it's there in case we ever need it
    public Ship()
    {
        name = "Generic Ship";
        hullMaxHP = 10;
        hullHP = 10;
        hullAC = 10;
        sailsMaxHP = 10;
        sailsHP = 10;
        sailsAC = 10;
        crew = 10;
        maneuverability = 10;
        damageThreshold = 10;
        isAlly = true;
    }

    public Ship(String nameInput, int hullHPInput, int hullACInput, int sailsHPInput, int sailsACInput, int crewInput, int maneuverabilityInput, int damageThresholdInput, boolean isAllyInput)
    {
        name = nameInput;
        hullMaxHP = hullHPInput;
        hullHP = hullMaxHP;
        hullAC = hullACInput;
        sailsMaxHP = sailsHPInput;
        sailsHP = sailsMaxHP;
        sailsAC = sailsACInput;
        crew = crewInput;
        maneuverability = maneuverabilityInput;
        damageThreshold = damageThresholdInput;
        isAlly = isAllyInput;
    }

    //Get the 'Status' of the ship
    //NAME (20 chars) | Hull HP (%Remaining) (15 chars) | Sail HP (%Remaining) (15 chars) | Health Bar (char count may/may not matter)
    public String getStatus()
    {
        String output = "";
        double hullPercent = ((double)hullHP / (double)hullMaxHP) * 100;
        double sailsPercent = ((double)sailsHP / (double)sailsMaxHP) * 100;

        output = output.concat(name);
        int addSpaces = (20 - name.length());
        for(int i=0;i<addSpaces;i++)
            output = output.concat(" ");

        //Display Hull HP Bar
        output = output.concat(" | ");
        //Pad out any missing digits with spaces
        if(hullHP < 10)
            output = output.concat(" ");
        if(hullHP < 100)
            output = output.concat(" ");
        if(hullHP < 1000)
            output = output.concat(" ");
        if(hullHP < 10000)
            output = output.concat(" ");
        output = output.concat(hullHP + " HP ");

        if(hullPercent > 95)
            output = output.concat("████████████████████");
        else if(hullPercent > 85)
            output = output.concat("██████████████████░░");
        else if(hullPercent > 75)
            output = output.concat("████████████████░░░░");
        else if(hullPercent > 65)
            output = output.concat("██████████████░░░░░░");
        else if(hullPercent > 55)
            output = output.concat("████████████░░░░░░░░");
        else if(hullPercent > 45)
            output = output.concat("██████████░░░░░░░░░░");
        else if(hullPercent > 35)
            output = output.concat("████████░░░░░░░░░░░░");
        else if(hullPercent > 25)
            output = output.concat("██████░░░░░░░░░░░░░░");
        else if(hullPercent > 15)
            output = output.concat("████░░░░░░░░░░░░░░░░");
        else if(hullPercent > 5)
            output = output.concat("██░░░░░░░░░░░░░░░░░░");
        else if(hullPercent > 0)
            output = output.concat("░░░░░░░░░░░░░░░░░░░░");


        output = output.concat(" | ");
        //Pad out any missing digits with spaces
        if(sailsHP < 10)
            output = output.concat(" ");
        if(sailsHP < 100)
            output = output.concat(" ");
        if(sailsHP < 1000)
            output = output.concat(" ");
        if(sailsHP < 10000)
            output = output.concat(" ");
        output = output.concat(sailsHP + " HP ");

        if(sailsPercent > 95)
            output = output.concat("████████████████████");
        else if(sailsPercent > 85)
            output = output.concat("██████████████████░░");
        else if(sailsPercent > 75)
            output = output.concat("████████████████░░░░");
        else if(sailsPercent > 65)
            output = output.concat("██████████████░░░░░░");
        else if(sailsPercent > 55)
            output = output.concat("████████████░░░░░░░░");
        else if(sailsPercent > 45)
            output = output.concat("██████████░░░░░░░░░░");
        else if(sailsPercent > 35)
            output = output.concat("████████░░░░░░░░░░░░");
        else if(sailsPercent > 25)
            output = output.concat("██████░░░░░░░░░░░░░░");
        else if(sailsPercent > 15)
            output = output.concat("████░░░░░░░░░░░░░░░░");
        else if(sailsPercent > 5)
            output = output.concat("██░░░░░░░░░░░░░░░░░░");
        else if(sailsPercent > 0)
            output = output.concat("░░░░░░░░░░░░░░░░░░░░");

        output = output.concat(" | ");
        if(crew < 10)
            output = output.concat(" ");
        if(crew < 100)
            output = output.concat(" ");
        if(crew < 1000)
            output = output.concat(" ");
        output = output.concat(String.valueOf(crew));

        return output;
    }

    public void initialize()
    {
        //Relevant Variable Declaration
        Scanner scanner = new Scanner(System.in);
        String csInput = "";
        boolean customShipLoop = true; //Boolean to track whether or not to break out of the custom ship while loops
        boolean cancelSelected = false;

        //Get ship name
        while (customShipLoop)
        {
            System.out.println("Enter the ship name (or CANCEL to exit selection):");
            csInput = scanner.nextLine();
            if(csInput.equalsIgnoreCase("CANCEL"))
            {
                cancelSelected = true;
                customShipLoop = false;
            }
            else if(csInput.length() > 20)
            {
                System.out.println("ERROR: Ship name cannot be more than 20 characters long!");
            }
            else if(csInput.length() < 1)
            {
                System.out.println("ERROR: Ship name cannot be empty!");
            }
            else
            {
                name = csInput;
                customShipLoop = false;
            }
        }

        if(!cancelSelected)
        {
            //Get ship hull HP
            customShipLoop = true;
            while(customShipLoop)
            {
                boolean invalidInput = false;
                System.out.println("Enter the Hull HP for " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }

                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    customShipLoop = false;
                    cancelSelected = true;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid input!");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) <= 0)
                {
                    System.out.println("ERROR: Ship Hull HP cannot be 0 or less!");
                }
                else if(Integer.parseInt(csInput) > 99999)
                {
                    System.out.println("ERROR: Ship Hull HP cannot be greater than 99999!");
                }
                else
                {
                    hullHP = Integer.parseInt(csInput);
                    hullMaxHP = hullHP;
                    customShipLoop = false;
                }
            }
        }
        //Get Ship Hull AC
        if(!cancelSelected)
        {
            customShipLoop = true;
            boolean invalidInput = false;
            while(customShipLoop)
            {
                System.out.println("Enter the Hull AC for " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }
                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    cancelSelected = true;
                    customShipLoop = false;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid Input");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) < 0)
                {
                    System.out.println("ERROR: Hull AC cannot be less than 0!");
                }
                else
                {
                    hullAC = Integer.parseInt(csInput);
                    customShipLoop = false;
                }
            }
        }

        //Get ship sail HP
        if(!cancelSelected)
        {
            customShipLoop = true;
            while(customShipLoop)
            {
                boolean invalidInput = false;
                System.out.println("Enter the Sail HP for " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }

                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    customShipLoop = false;
                    cancelSelected = true;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid input!");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) <= 0)
                {
                    System.out.println("ERROR: Ship Sail HP cannot be 0 or less!");
                }
                else if(Integer.parseInt(csInput) > 99999)
                {
                    System.out.println("ERROR: Ship Sail HP cannot be greater than 99999!");
                }
                else
                {
                    sailsHP = Integer.parseInt(csInput);
                    sailsMaxHP = sailsHP;
                    customShipLoop = false;
                }
            }
        }

        //Get Ship Sail AC
        if(!cancelSelected)
        {
            customShipLoop = true;
            boolean invalidInput = false;
            while(customShipLoop)
            {
                System.out.println("Enter the Sail AC for " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }
                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    cancelSelected = true;
                    customShipLoop = false;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid Input");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) < 0)
                {
                    System.out.println("ERROR: Sail AC cannot be less than 0!");
                }
                else
                {
                    sailsAC = Integer.parseInt(csInput);
                    customShipLoop = false;
                }
            }
        }

        //Get Ship Maneuverability
        if(!cancelSelected)
        {
            customShipLoop = true;
            boolean invalidInput = false;
            while(customShipLoop)
            {
                System.out.println("Enter the maneuverability for " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }
                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    cancelSelected = true;
                    customShipLoop = false;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid Input");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) < 0)
                {
                    System.out.println("ERROR: Maneuverability cannot be less than 0!");
                }
                else
                {
                    maneuverability = Integer.parseInt(csInput);
                    customShipLoop = false;
                }
            }
        }

        //Get Ship Crew
        if(!cancelSelected)
        {
            customShipLoop = true;
            boolean invalidInput = false;
            while(customShipLoop)
            {
                System.out.println("Enter the number of crew on " + name + " or enter 'CANCEL' to exit ship creation");
                csInput = scanner.nextLine();
                try
                {
                    Integer.parseInt(csInput);
                }
                catch(Exception e)
                {
                    invalidInput = true;
                }
                if(csInput.equalsIgnoreCase("CANCEL"))
                {
                    cancelSelected = true;
                    customShipLoop = false;
                }
                else if(invalidInput)
                {
                    System.out.println("ERROR: Invalid Input");
                    invalidInput = false;
                }
                else if(Integer.parseInt(csInput) < 0)
                {
                    System.out.println("ERROR: Number of crew cannot be less than 0!");
                }
                else if(Integer.parseInt(csInput) > 9999)
                {
                    System.out.println("ERROR: Number of crew cannot be greater than 9999!");
                }
                else
                {
                    crew = Integer.parseInt(csInput);
                    customShipLoop = false;
                }
            }
        }

        //Get 'faction' of the ship
        if(!cancelSelected)
        {
            customShipLoop = true;
            while (customShipLoop)
            {
                System.out.println("Is this ship an ally or enemy?:\n1) Ally\n2) Enemy");
                csInput = scanner.nextLine();
                if(csInput.equalsIgnoreCase("1"))
                {
                    isAlly = true;
                    customShipLoop = false;
                }
                else if(csInput.equalsIgnoreCase("2"))
                {
                    isAlly = false;
                    customShipLoop = false;
                }
                else System.out.println("ERROR: Invalid Input!");
            }
        }

        //If cancel was selected, set crew to an impossible number (we can track this later on)
        if(cancelSelected)
            crew = 10000;
    }
}