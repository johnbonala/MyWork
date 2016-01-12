package com.lifelens.webelements;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.lifelens.browsers.WebElementProcessor;
import com.lifelens.exceptions.ExceptionErrorCodes;
import com.lifelens.exceptions.TestAutomationException;
import com.lifelens.globals.GlobalCSSIdentifiers;
import com.lifelens.htmlparser.HtmlParser;
import com.lifelens.interpreter.ArgList;
import com.lifelens.interpreter.TestContext;

/**
 * Represents the SwimLane object in lifelen's
 * 
 * This helps to use swim lane components.
 * 
 * @author Pratik.Gilda
 * 
 * @version 1.0
 * 
 * @since 01.07.2014
 * 
 */
public class SwimLane {

	private static Logger logger = Logger.getLogger(Label.class.getName());
	private WebDriver browser;
	private String containerName;
	private String containerIndex;
	private TestContext testContext;

	/**
	 * Instantiates a swim lane
	 * 
	 * @param browser
	 * @throws TestAutomationException
	 */
	public SwimLane(WebDriver browser, TestContext testContext, String containerNameAndIndex)
			throws TestAutomationException {
		this.browser = browser;
		this.testContext = testContext;

		ArgList argList = new ArgList(containerNameAndIndex);
		containerName = argList.getParameter(0);
		if (argList.getParmetersCount() == 2) {
			containerIndex = argList.getParameter(1);
		}
	}

	/**
	 * This method verifies whether the provided swimlane option is exist in the
	 * given container
	 * 
	 * @param swimLaneOption
	 * @return optionFound
	 * @throws TestAutomationException
	 */
	public boolean verifySwimlaneOption(String swimLaneOption) throws TestAutomationException {

		logger.debug("ListBox: moveOptionToContainer start ");

		boolean optionFound = false;

		// default container index is 1 i.e. first appearance
		int parsedContainerIndex = 1;

		if (containerIndex != null) {
			parsedContainerIndex = Integer.parseInt(containerIndex);
		}

		List<WebElement> options = getSourceContainersAndItems();

		// If the container name is from source container list verify from
		// source container only
		if (new HtmlParser(options.get(0)).getinnerText().contains(containerName)) {

			if (!options.isEmpty() && options != null) {
				if (new HtmlParser(options.get(parsedContainerIndex - 1)).getinnerText().contains(swimLaneOption)) {
					optionFound = true;
				} else {
					throw new TestAutomationException(
							ExceptionErrorCodes.TA055_WEB_ELEMENT_PROCESSING_COMMON_INVALID_INPUT_INDEX_FOR_SELECT_ERROR,
							"Invalid index is passed to select option text");
				}
			}
		} else {
			options = getTargetContainersAndItems();

			if (!options.isEmpty() && options != null) {
				if (new HtmlParser(options.get(parsedContainerIndex - 1)).getinnerText().contains(swimLaneOption)) {
					optionFound = true;
				} else {
					throw new TestAutomationException(
							ExceptionErrorCodes.TA055_WEB_ELEMENT_PROCESSING_COMMON_INVALID_INPUT_INDEX_FOR_SELECT_ERROR,
							"Invalid index is passed to select option text");
				}
			}
		}

		logger.debug("ListBox: moveOptionToContainer end ");
		return optionFound;
	}

	/**
	 * Move's selected option back to passed container, use index if there are
	 * containers with a same name
	 * 
	 * @param optionName
	 * @throws TestAutomationException
	 */
	public boolean selectAndMoveOptionToListBox(String optionName) throws TestAutomationException {

		logger.debug("ListBox: moveOptionToContainer start ");

		String index;

		// default container index is 0 i.e. first appearance
		int parsedContainerIndex = 0;

		List<WebElement> targetListBoxLableList = getTargerListBoxLableList();
		if (containerIndex != null) {
			parsedContainerIndex = Integer.parseInt(containerIndex);
		}
		int containerNumber = checkContainerAvailabilty(targetListBoxLableList, containerName, parsedContainerIndex);

		index = Integer.toString(parsedContainerIndex);
		boolean isSelected = selectOptionFromList(optionName, index);
		boolean clickedAddButton = false;
		if (isSelected) {
			clickedAddButton = getAddButton(containerNumber);
		}

		logger.debug("ListBox: moveOptionToContainer end ");
		return clickedAddButton;
	}

	/**
	 * Move's selected option back from passed container, use index if there are
	 * containers with a same name
	 * 
	 * @param optionName
	 * @throws TestAutomationException
	 */
	public boolean selectAndMoveOptionFromListbox(String optionName) throws TestAutomationException {

		logger.debug("ListBox: moveOptionFromContainer start ");

		String index;
		int containerNumber;
		int parsedContainerIndex = 0;

		List<WebElement> targetListBoxLableList = getTargerListBoxLableList();

		if (containerIndex != null) {
			parsedContainerIndex = Integer.parseInt(containerIndex);
		}

		if (targetListBoxLableList != null) {
			containerNumber = checkContainerAvailabilty(targetListBoxLableList, containerName, parsedContainerIndex);
		} else {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA082_CONTAINER_ELEMENT_NOT_FOUND_BY_CONTAINER_NAME_ERROR,
					"Web elements with containerName name '" + containerName + "' not found");
		}

		index = Integer.toString(parsedContainerIndex);
		boolean selected = selectOptionFromList(optionName, index);
		boolean clickedRemoveButton = false;
		if (selected) {
			clickedRemoveButton = getRemoveButton(containerNumber);
		}

		logger.debug("ListBox: moveBenefitToContainer end ");
		return clickedRemoveButton;
	}

	private boolean getAddButton(int containerNumber) throws TestAutomationException {
		StringBuilder sb = new StringBuilder();
		sb.append(containerNumber);
		String index = sb.toString();
		boolean isAddButton = false;
		if ((new WebElementProcessor(browser, testContext).clickOnButton(GlobalCSSIdentifiers.ITEMADDBUTTOMN, index))) {
			isAddButton = true;
		}
		return isAddButton;
	}

	private boolean getRemoveButton(int containerNumber) throws TestAutomationException {
		StringBuilder sb = new StringBuilder();
		sb.append(containerNumber);
		String index = sb.toString();
		boolean isRemoveButton = false;
		if ((new WebElementProcessor(browser, testContext).clickOnButton(GlobalCSSIdentifiers.ITEMREMOVEBUTTON, index))) {
			isRemoveButton = true;
		}
		return isRemoveButton;
	}

	/**
	 * Returns all the Swimlane containers available in the web page
	 * 
	 * @return List<WebElement>: List of Swimlane containers
	 * @throws TestAutomationException
	 */
	public List<WebElement> getSwimLaneContainersList() throws TestAutomationException {

		List<WebElement> containerList = WebElementRetriever.getWebElementsByClassName(browser, containerName,
				GlobalCSSIdentifiers.SWIMLANECONTAINER);
		if (containerList != null && containerList.size() > 0) {
			return containerList;
		}
		throw new TestAutomationException(ExceptionErrorCodes.TA083_SWIMLANE_CONTAINER_NOT_FOUND_ERROR,
				"Web elements Swimlane Containers not found on page");
	}

	/**
	 * To get the swimlane container corresponds to in the web page
	 * 
	 * @param containerName
	 * @return index of the container
	 * @throws TestAutomationException
	 */
	public int getSwimLaneContainer(String containerName, String index) throws TestAutomationException {

		logger.debug("Start of getSwimLaneContainer ");
		List<WebElement> containerList = getSwimLaneContainersList();
		Iterator<WebElement> containerListIterator = containerList.iterator();
		WebElement container;
		int parsedIndex = Integer.parseInt(index);
		while (containerListIterator.hasNext()) {
			parsedIndex++;
			container = containerListIterator.next();
			if (container.isDisplayed()) {
				HtmlParser innerhtml = new HtmlParser(container);
				String webButtonName = innerhtml.getinnerText();
				if (webButtonName.contains(containerName)) {
					break;
				}
			}
		}
		logger.debug("End of Container: containerFound ");
		return parsedIndex;
	}

	/**
	 * This gives Source List Box
	 * 
	 * @return list of
	 * @throws TestAutomationException
	 */
	public List<WebElement> getTargerListBoxLableList() throws TestAutomationException {
		List<WebElement> targerListBoxLableList = WebElementRetriever.getWebElementsByClassName(browser, containerName,
				GlobalCSSIdentifiers.SWIMLANETARGETS);
		return targerListBoxLableList;
	}

	/**
	 * Checks for container name and returns container index
	 * 
	 * @param containerlist
	 * @param containerName
	 * 
	 * @return index
	 */
	private int checkContainerAvailabilty(List<WebElement> containerlist, String containerName, int conatinerIndex) {

		logger.debug("Start of Container: checkContainerAvailabilty ");
		Iterator<WebElement> allContainers = containerlist.iterator();
		WebElement container;

		if (conatinerIndex != 0) {
			return conatinerIndex;
		} else {
			while (allContainers.hasNext()) {
				conatinerIndex++;
				container = allContainers.next();
				if (container.isDisplayed()) {
					HtmlParser innerhtml = new HtmlParser(container);
					String webButtonName = innerhtml.getinnerText();
					if (webButtonName.contains(containerName)) {
						break;
					}
				}
			}
		}
		logger.debug("End of Container: containerFound ");
		return conatinerIndex;
	}

	/**
	 * Method to select option from list box
	 * 
	 * @param optionText
	 * @param index
	 *            (Optional)
	 * 
	 * @throws TestAutomationException
	 */
	private boolean selectOptionFromList(String optionText, String index) throws TestAutomationException {
		List<WebElement> options = getTargetListBoxOptions(optionText);
		WebElement option = null;
		if (StringUtils.isBlank(index) || index.equals("0")) {
			index = "1";
		}

		try {
			if (options.size() > 1) {
				option = options.get(Integer.parseInt(index) - 1);
			} else {
				option = options.get(0);
			}
		} catch (NumberFormatException e) {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA055_WEB_ELEMENT_PROCESSING_COMMON_INVALID_INPUT_INDEX_FOR_SELECT_ERROR,
					"Invalid input parameter passed to select option text");
		}

		if (option != null) {
			option.click();
			return true;
		} else {
			throw new TestAutomationException(
					ExceptionErrorCodes.TA056_WEB_ELEMENT_PROCESSING_COMMON_OPTION_NOT_AVAILABLE_FOR_SELECT_ERROR,
					"Invalid input parameter passed to select option text");
		}
	}

	/**
	 * gives the target list box options for the given target Label
	 * 
	 * @param targetLableName
	 * @return List of target ListBox options list
	 * @throws TestAutomationException
	 */
	private List<WebElement> getTargetListBoxOptions(String targetLableName) throws TestAutomationException {
		List<WebElement> targetListBoxOptions = WebElementRetriever.getWebElementsByXPath(browser, targetLableName,
				"//select/option[text()='" + targetLableName + "']");
		return targetListBoxOptions;
	}

	/**
	 * Returns SwimLane Source containers and available items
	 * 
	 * @return source containers and items list
	 * @throws TestAutomationException
	 */
	public List<WebElement> getSourceContainersAndItems() throws TestAutomationException {
		List<WebElement> swimlaneSourceContainerLabels = WebElementRetriever.getWebElementsByClassName(browser,
				containerName, GlobalCSSIdentifiers.AVAILABLEITEMS);
		return swimlaneSourceContainerLabels;
	}

	/**
	 * Returns SwimLane target containers and selected items
	 * 
	 * @return List: getTargetContainersAndItems
	 * @throws TestAutomationException
	 */
	public List<WebElement> getTargetContainersAndItems() throws TestAutomationException {
		List<WebElement> swimlaneTargetContainerLabels = WebElementRetriever.getWebElementsByClassName(browser,
				containerName, GlobalCSSIdentifiers.SELECTEDITEMS);
		return swimlaneTargetContainerLabels;
	}
}
