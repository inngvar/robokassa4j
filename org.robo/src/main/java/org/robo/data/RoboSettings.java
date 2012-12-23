package org.robo.data;

/**
 * Различные настройки робокассы
 * 
 * @author igor.ch
 * 
 */
public interface RoboSettings {

	/**
	 * Получить описание товара
	 * 
	 * @return описание товара
	 */
	String getDescription();

	/**
	 * Валюта оплаты
	 * 
	 * @return валюта оплаты
	 */
	String getCurrency();

	/**
	 * культура
	 * 
	 * @return культура (en ru)
	 */
	String getCulture();

	/**
	 * Логин мерчента в системе робокасса
	 * 
	 * @return логин магазина
	 */
	String getLogin();

	/**
	 * Пароль 1 от Робокассы
	 * 
	 * @return Пароль 1 от Робокассы
	 */
	String getPass1();

	/**
	 * Пароль 2 от Робокассы
	 * 
	 * @return Пароль 2 от Робокассы
	 */
	String getPass2();

}
