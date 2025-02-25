package com.gog.civilregistry.adoption.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

public  class CommonUtils {
	
	
	/**
	 * Date Difference Method
	 * 
	 * @param start_date
	 * @param end_date
	 * @return
	 */
	public static long findDifferenceBetweenTwoDates(String startDate,String endDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
//		System.out.println("start date:"+startDate);
//		System.out.println("end date:"+endDate);
		long finalDiff  = 0;
		try {
			Date d1 = sdf.parse(startDate);
			Date d2 = sdf.parse(endDate);
			
			long diff = d2.getTime() - d1.getTime();
		    long diffSeconds = diff / 1000 % 60;
		    long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			if(format.contains("HH")) {
				finalDiff=diffHours;				
			}
			else if(format.contains("MM")) {
				finalDiff=diffMinutes;
			}
		    		
		}
		catch (Exception e) {
			//e.printStackTrace();
		}
		return finalDiff;
	}
	
	public static String getCurrentDateTimeWithoutTimeZone(String format)  {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = new Date();
		String curDate = sdf.format(date);
		return curDate;
		 
	}
	
	/**
	 * Method to generate ten-character password, each with a minimum of two lower
	 * case characters, two upper case characters, two digits, and two special
	 * characters.
	 * 
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public static String generateDefaultPassword(int len) throws Exception {
		String password = null;
		if (len < 8) {
			throw new Exception("Password length cannot be less than 8 characters");
		} else {
			PasswordGenerator passwordGenerator = new PasswordGenerator();

			CharacterRule lowerCaseRule = constructLowerCaseComponent();

			CharacterRule upperCaseRule = constructUpperCaseComponent();

			CharacterRule digitRule = constructDigitComponent();

			CharacterRule splCharacterRule = constructSpecialCharacterComponent();

			password = passwordGenerator.generatePassword(len, splCharacterRule, lowerCaseRule, upperCaseRule,
					digitRule);

			return password;

		}
	}

	/**
	 * @return
	 */
	private static CharacterRule constructSpecialCharacterComponent() {

		CharacterData specialChars = new CharacterData() {
			public String getErrorCode() {
				return "ERROR_CODE";
			}

			public String getCharacters() {
				return "!@#$%^&*()_+";
			}
		};

		CharacterRule splCharRule = new CharacterRule(specialChars);
		splCharRule.setNumberOfCharacters(2);
		return splCharRule;
	}

	/**
	 * @return
	 */
	private static CharacterRule constructDigitComponent() {
		CharacterData digitChars = EnglishCharacterData.Digit;
		CharacterRule digitRule = new CharacterRule(digitChars);
		digitRule.setNumberOfCharacters(2);
		return digitRule;
	}

	/**
	 * @return
	 */
	private static CharacterRule constructUpperCaseComponent() {
		CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
		CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
		upperCaseRule.setNumberOfCharacters(2);
		return upperCaseRule;
	}

	/**
	 * @return
	 */
	private static CharacterRule constructLowerCaseComponent() {
		CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
		CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
		lowerCaseRule.setNumberOfCharacters(2);
		return lowerCaseRule;
	}
	
	public static String generateRandomPassword(int len) {
		final String chars = "0123456789";
		return RandomStringUtils.random(len, chars);
	}

}
