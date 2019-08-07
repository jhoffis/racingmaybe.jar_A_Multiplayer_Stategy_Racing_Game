package handlers;

import elem.Bank;
import elem.Car;
import elem.Player;
import elem.Upgrades;

/**
 * 
 * @author jhoffis
 * 
 *         Returns plain text and whatever that the actual scene just shows. The
 *         FixCar scene should not deal with figuring out text, just printing.
 */

public class FixCarHandler {
	private Upgrades upgrades;
	private String[] upgradeNames;
	private int currentUpgrade;
	
	public FixCarHandler() {
		upgrades = new Upgrades();
		upgradeNames = upgrades.getUpgradeNames();
	}
	
	public String getUpgradeName(int i) {
		return upgradeNames[i];
	}

	public String selectUpgrade(int i) {
		currentUpgrade = i;
		return null;
	}
	
	public String selectUpgrade(String upgradeName, Car car, Bank bank) {
		for(int i = 0; i < upgradeNames.length; i++) {
			if(upgradeName.equals(getUpgradeName(i)))
				currentUpgrade = i;
		}
		
		String upgradeText = "<html>UPGRADED " + upgrades.getUpgradedStats(currentUpgrade, car) + "<br/><br/>$" + upgrades.getCostMoney(currentUpgrade, bank) + " or "
		+ upgrades.getCostPoints(currentUpgrade, bank) + " points </html>";
		
		return upgradeText;
	}

	public void buyMoney(Car car, Bank bank) {
		
	}

	public void buyPoints(Car car, Bank bank) {
		
	}

	public String[] getUpgradeNames() {
		return upgradeNames;
	}

	public void setUpgradeNames(String[] upgradeNames) {
		this.upgradeNames = upgradeNames;
	}
}
