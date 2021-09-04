public class Attack
{
    public String name;
    public String attackerName;
    public int type;
    public int numDice;
    public int numFaces;
    public int attackMod;
    public int damageMod;
    public boolean targetSails;

    public Attack(String nameInput, String attackerName, int typeInput, int dice, int faces, int attack, int damage, boolean sails)
    {
        name = nameInput;
        this.attackerName = attackerName;
        type = typeInput;
        numDice = dice;
        numFaces = faces;
        attackMod = attack;
        damageMod = damage;
        targetSails = sails;
    }

    public Attack()
    {
        name = "";
        attackerName = "";
        type = 1;
        numDice = 1;
        numFaces = 1;
        attackMod = 0;
        damageMod = 0;
        targetSails = false;
    }

}
