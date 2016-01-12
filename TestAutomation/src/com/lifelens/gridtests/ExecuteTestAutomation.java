package com.lifelens.gridtests;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lifelens.globals.Global;

/**
 * This class is used to design GUI for test automation
 * 
 * @author venkata.kintali(CO54151)
 * 
 * @since 05.06.2015
 * 
 */
class ExecuteTestAutomation {

	private JPanel panel;
	private JFrame jf;
	private JButton runAutomation;
	private Test03 test3;
	private JLabel frameNameLab, midLandsLab, ibmLab, tuiLab, telefonicaLab, tbpeCommerceLab, heineKenLab, scbLab,
			v3adminPensionLab, v3E2ERegLab, unBundledLab, slLab, lifeLinesLab, v3ReportingLab;
	private JCheckBox MidlandsChk, ibmChk, tuiChk, telefonicaChk, tbpeCommerceChk, hukChk, scbChk, nationalGridChk,
			v3adminPensionChk, V3ReportingChk, V3E2EChk, slUnbundledChk;

	private ArrayList<JCheckBox> getSelectedCompanyes = new ArrayList<JCheckBox>();

	public ExecuteTestAutomation() {
		initComponents();
	}

	public static void main(String args[]) {
		new ExecuteTestAutomation();
	}

	public void initComponents() {
		jf = new javax.swing.JFrame("Test Automation");
		panel = new javax.swing.JPanel();
		jf.add(panel);
		panel.setBackground(new Color(191, 239, 255));
		panel.setLayout(null);
		jf.setSize(1824, 1024);
		jf.show();

		// adding labels to panel

		frameNameLab = new javax.swing.JLabel("Test Automation");
		frameNameLab.setFont(new Font("Dialog", Font.ITALIC, 24));
		frameNameLab.setBounds(460, 20, 400, 40);
		panel.add(frameNameLab);

		lifeLinesLab = new javax.swing.JLabel("LifeLens");
		lifeLinesLab.setFont(new Font("Dialog", Font.ITALIC, 24));
		lifeLinesLab.setBounds(150, 50, 400, 40);
		panel.add(lifeLinesLab);

		midLandsLab = new javax.swing.JLabel("Midlands");
		midLandsLab.setFont(new Font("Dialog", Font.BOLD, 20));
		midLandsLab.setBounds(100, 80, 150, 40);
		panel.add(midLandsLab);

		ibmLab = new javax.swing.JLabel("IBM");
		ibmLab.setFont(new Font("Dialog", Font.BOLD, 20));
		ibmLab.setBounds(100, 140, 150, 40);
		panel.add(ibmLab);

		tuiLab = new javax.swing.JLabel("TUI");
		tuiLab.setFont(new Font("Dialog", Font.BOLD, 20));
		tuiLab.setBounds(100, 200, 150, 40);
		panel.add(tuiLab);

		telefonicaLab = new javax.swing.JLabel("Telefonica");
		telefonicaLab.setFont(new Font("Dialog", Font.BOLD, 20));
		telefonicaLab.setBounds(100, 260, 150, 40);
		panel.add(telefonicaLab);

		tbpeCommerceLab = new javax.swing.JLabel(" TBP eCommerce  ");
		tbpeCommerceLab.setFont(new Font("Dialog", Font.BOLD, 20));
		tbpeCommerceLab.setBounds(100, 320, 150, 40);
		panel.add(tbpeCommerceLab);

		heineKenLab = new javax.swing.JLabel(" HeineKen ");
		heineKenLab.setFont(new Font("Dialog", Font.BOLD, 20));
		heineKenLab.setBounds(100, 380, 150, 40);
		panel.add(heineKenLab);

		scbLab = new javax.swing.JLabel("SCB");
		scbLab.setFont(new Font("Dialog", Font.BOLD, 20));
		scbLab.setBounds(100, 440, 150, 40);
		panel.add(scbLab);

		v3adminPensionLab = new javax.swing.JLabel("NationalGrid");
		v3adminPensionLab.setFont(new Font("Dialog", Font.BOLD, 20));
		v3adminPensionLab.setBounds(100, 500, 150, 40);
		panel.add(v3adminPensionLab);

		// adding check boxes to panel

		MidlandsChk = new JCheckBox("Midlands");
		MidlandsChk.setFont(new Font("Dialog", Font.BOLD, 16));
		MidlandsChk.setBounds(350, 95, 20, 20);
		panel.add(MidlandsChk);

		ibmChk = new JCheckBox("IBM");
		ibmChk.setFont(new Font("Dialog", Font.BOLD, 20));
		ibmChk.setBounds(350, 140, 20, 20);
		panel.add(ibmChk);

		tuiChk = new JCheckBox("TUI");
		tuiChk.setFont(new Font("Dialog", Font.BOLD, 20));
		tuiChk.setBounds(350, 200, 20, 20);
		panel.add(tuiChk);

		telefonicaChk = new JCheckBox("Telefonica");
		telefonicaChk.setFont(new Font("Dialog", Font.BOLD, 20));
		telefonicaChk.setBounds(350, 260, 20, 20);
		panel.add(telefonicaChk);

		tbpeCommerceChk = new JCheckBox("TBP eCommerce");
		tbpeCommerceChk.setFont(new Font("Dialog", Font.BOLD, 20));
		tbpeCommerceChk.setBounds(350, 320, 20, 20);
		panel.add(tbpeCommerceChk);

		hukChk = new JCheckBox("HUK");
		hukChk.setFont(new Font("Dialog", Font.BOLD, 20));
		hukChk.setBounds(350, 380, 20, 20);
		panel.add(hukChk);

		scbChk = new JCheckBox("SCB");
		scbChk.setFont(new Font("Dialog", Font.BOLD, 20));
		scbChk.setBounds(350, 440, 20, 20);
		panel.add(scbChk);

		nationalGridChk = new JCheckBox("NationalGrid");
		nationalGridChk.setFont(new Font("Dialog", Font.BOLD, 20));
		nationalGridChk.setBounds(350, 500, 20, 20);
		panel.add(nationalGridChk);

		lifeLinesLab = new javax.swing.JLabel("V3");
		lifeLinesLab.setFont(new Font("Dialog", Font.ITALIC, 24));
		lifeLinesLab.setBounds(720, 50, 400, 40);
		panel.add(lifeLinesLab);

		v3adminPensionLab = new javax.swing.JLabel("V3AdminPension");
		v3adminPensionLab.setFont(new Font("Dialog", Font.BOLD, 20));
		v3adminPensionLab.setBounds(680, 80, 150, 40);
		panel.add(v3adminPensionLab);

		v3ReportingLab = new javax.swing.JLabel("V3 Reporting");
		v3ReportingLab.setFont(new Font("Dialog", Font.BOLD, 20));
		v3ReportingLab.setBounds(680, 140, 150, 40);
		panel.add(v3ReportingLab);

		v3E2ERegLab = new javax.swing.JLabel("V3 End two End Regression");
		v3E2ERegLab.setFont(new Font("Dialog", Font.BOLD, 20));
		v3E2ERegLab.setBounds(680, 200, 150, 40);
		panel.add(v3E2ERegLab);

		unBundledLab = new javax.swing.JLabel("UNBUNDLED");
		unBundledLab.setFont(new Font("Dialog", Font.BOLD, 20));
		unBundledLab.setBounds(680, 260, 150, 40);
		panel.add(unBundledLab);

		slLab = new javax.swing.JLabel("SL");
		slLab.setFont(new Font("Dialog", Font.BOLD, 20));
		slLab.setBounds(680, 320, 150, 40);
		panel.add(slLab);

		// scbLab = new javax.swing.JLabel("SCB");
		// scbLab.setFont(new Font("Dialog", Font.BOLD, 20));
		// scbLab.setBounds(680, 380, 150, 40);
		// panel.add(scbLab);

		v3adminPensionChk = new JCheckBox("V3AdminPension");
		v3adminPensionChk.setFont(new Font("Dialog", Font.BOLD, 16));
		v3adminPensionChk.setBounds(650, 90, 20, 20);
		panel.add(v3adminPensionChk);

		V3ReportingChk = new JCheckBox("V3 Reporting");
		V3ReportingChk.setFont(new Font("Dialog", Font.BOLD, 16));
		V3ReportingChk.setBounds(650, 150, 20, 20);
		panel.add(V3ReportingChk);

		V3E2EChk = new JCheckBox("V3 E2E");
		V3E2EChk.setFont(new Font("Dialog", Font.BOLD, 16));
		V3E2EChk.setBounds(650, 210, 20, 20);
		panel.add(V3E2EChk);

		slUnbundledChk = new JCheckBox("SL_Unbundled");
		slUnbundledChk.setFont(new Font("Dialog", Font.BOLD, 16));
		slUnbundledChk.setBounds(650, 330, 20, 20);
		panel.add(slUnbundledChk);

		// SCBChk = new JCheckBox("SCB");
		// SCBChk.setFont(new Font("Dialog", Font.BOLD, 16));
		// SCBChk.setBounds(650, 390, 20, 20);
		// panel.add(SCBChk);

		// adding button to panel
		runAutomation = new javax.swing.JButton("Start Test Automation");
		runAutomation.setFont(new Font("Dialog", Font.BOLD, 16));
		runAutomation.setBounds(460, 620, 220, 30);
		panel.add(runAutomation);

		// Add action listener to button
		runAutomation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBoxesNames[] = { MidlandsChk, ibmChk, tuiChk, telefonicaChk, tbpeCommerceChk, hukChk,
						scbChk, nationalGridChk, v3adminPensionChk, V3ReportingChk, V3E2EChk, slUnbundledChk };
				// getting the selected company names from layout
				for (int i = 0; i <= checkBoxesNames.length - 1; i++) {
					if (checkBoxesNames[i].isSelected()) {
						getSelectedCompanyes.add(checkBoxesNames[i]);
					}
				}

				if (getSelectedCompanyes.size() > 0) {
					if (getSelectedCompanyes.size() == 1) {
						String selectedCompany = getSelectedCompanyes.get(0).getText();
						Global.setTestlab(selectedCompany);
						test3 = new Test03(selectedCompany);
						test3.OpenTestSet();
						test3.ExecuteTest();
						test3.CloseTestSet();
					}
				} else {
					JOptionPane.showMessageDialog(jf, "You have not selected any company");
				}
			}
		});
	}
}
