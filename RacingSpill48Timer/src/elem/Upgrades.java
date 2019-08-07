package elem;

public class Upgrades {
	
	private String[] upgradeNames = { "Upgrade cylinders", "Weight reduction bro", "Better fuel", "Bigger turbo", "More NOS",
			"Lighter pistons", "Grippier tyres and gears", "Beefier block" };
	private UpgradePrice[] upgradePrices;
	
	public Upgrades() {
		upgradePrices = new UpgradePrice[upgradeNames.length];
		for(UpgradePrice up : upgradePrices) {
			up = new UpgradePrice(1, 50);
		}
	}
	

	public boolean upgradeCylinders(Car car, Bank bank, boolean points) {
		upgradedCar.setHp(upgradedCar.getHp() + 75f);

	}

	public boolean upgradeWeight(Car car, Bank bank, boolean points) {
		currentTypeCart = 1;
		cost(80, 1);

		upgradedCar.setWeightloss(upgradedCar.getWeightloss() + (upgradedCar.getTotalWeight() / 10f));
	}

	public boolean upgradeFuel(Car car, Bank bank, boolean points) {
		currentTypeCart = 2;
		cost(40, 1);

		upgradedCar.setHp(upgradedCar.getHp() + (100f / (player.getInflation()[currentTypeCart] + 1f)));
	}

	public boolean upgradeTurbo(Car car, Bank bank, boolean points) {
		currentTypeCart = 3;
		cost(160, 1);

		upgradedCar.setHasTurbo(true);
		upgradedCar.setHp(upgradedCar.getHp() + (400f / (player.getInflation()[currentTypeCart] + 1f)));
		upgradedCar.setWeightloss(upgradedCar.getWeightloss() - 15);
	}

	public boolean upgradeNOS(Car car, Bank bank, boolean points) {
		currentTypeCart = 4;
		cost(40, 1);
		if (!upgradedCar.isHasNOS()) {
			upgradedCar.setHasNOS(true);
			upgradedCar.setNosAmountLeftStandard(1);
		}
		upgradedCar.setNosStrengthStandard(upgradedCar.getNosStrengthStandard() + 0.5);
	}

	public boolean upgradePistons(Car car, Bank bank, boolean points) {
		currentTypeCart = 5;
		cost(100, 1);

		upgradedCar.setWeightloss(upgradedCar.getWeightloss() + 50);
		upgradedCar.setHp(upgradedCar.getHp() + 75f);
	}

	public boolean upgradeGears(Car car, Bank bank, boolean points) {
		currentTypeCart = 6;
		cost(100, 1);

		double topspeedPrev = upgradedCar.getTopSpeed();
		double topspeedInc = 75.0;

		upgradedCar.setWeightloss(upgradedCar.getWeightloss() + 50);
		upgradedCar.setTopSpeed(topspeedPrev + topspeedInc);
		upgradedCar.setGearsbalance(upgradedCar.getGearsbalance() * (1 - (topspeedInc / topspeedPrev)));
		upgradedCar.setUpgradedGears(true);
	}

	public boolean upgradeBlock(Car car, Bank bank, boolean points) {
		currentTypeCart = 7;
		cost(120, 1);

		upgradedCar.setHp(upgradedCar.getHp() + (200f / (player.getInflation()[currentTypeCart] + 1f)));
		upgradedCar.setWeightloss(upgradedCar.getWeightloss() - 15);
	}

	public boolean upgradeAero(Car car, Bank bank, boolean points) {

	}

	public int getCostMoney(int i, Bank bank) {
		return (int) (upgradePrices[i].getMoney() * 0.75f * (bank.getInflation()[i] + 1f));
	}

	public int getCostPoints(int i, Bank bank) {
		return (int) (upgradePrices[i].getPoints() * (bank.getInflation()[i] + 1f));;
	}
	
	public String getUpgradedStats(int i, Car car) {
		return null;
	}

	public String[] getUpgradeNames() {
		return upgradeNames;
	}

	public void setUpgradeNames(String[] upgradeNames) {
		this.upgradeNames = upgradeNames;
	}

}
