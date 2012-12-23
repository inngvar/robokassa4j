package org.robo.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.robo.data.FailedInfo;
import org.robo.data.RequestData;
import org.robo.data.ResultInfo;
import org.robo.data.RoboSettings;
import org.robo.data.SuccessInfo;
import org.robo.factory.RequestFactory;

/**
 * Сервис работы с робокассой
 * 
 * @author igor.ch
 * 
 */
public abstract class RoboService {

	public String mrchLogin;
	private OperationServiceProvider provider;
	private RoboSettings settings;
	private RequestFactory requestFactory;

	public RoboService(OperationServiceProvider provider, RoboSettings settings, RequestFactory requestFactory) {
		this.provider = provider;
		this.settings = settings;
		this.requestFactory = requestFactory;
	}

	public boolean failed(FailedInfo data) {
		try {
			return provider.setFailed(data);
		} catch (Exception ex) {
			provider.processException(ex, "failed");
			return false;
		}

	}

	/**
	 * 
	 * @return культура для пользователя
	 */
	private String getCulture() {
		return settings.getCulture();
	}

	/**
	 * Валюта по умолчанию
	 * 
	 * @return
	 */
	private String getCurrency() {
		return settings.getCurrency();
	}

	/**
	 * 
	 * @return Описание товара
	 */
	private String getDescription() {
		return settings.getDescription();
	}

	private String getHash(StringBuilder builder)
			throws NoSuchAlgorithmException {
		final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(builder.toString().getBytes(
				Charset.forName("UTF8")));
		final byte[] resultByte = messageDigest.digest();
		final String result = new String(Hex.encodeHex(resultByte));
		return result.toUpperCase();
	}

	/**
	 * @return Возвращает логин мерчента
	 */
	private String getMrchLogin() {
		return settings.getLogin();
	}

	/**
	 * Получить данные для запроса на сервер Робокассы.
	 * 
	 * @return Данные для построения запроса инициализации оплаты к робокассе
	 * @throws NoSuchAlgorithmException
	 */
	public RequestData getRequestData(String sum)
			throws NoSuchAlgorithmException {
		try {
			int invoceId = provider.createOperation(Double.parseDouble(sum));
			RequestData data = requestFactory.create(invoceId,getMrchLogin(),sum,getCulture(),getCurrency(),getDescription());
					//new RequestDataImpl();
			/*
			 * 
			 
			data.setInvId(String.valueOf(invoceId));
			data.setMrchLogin(getMrchLogin());
			data.setOutSum(sum);
			data.setCulture(getCulture());
			data.setIncCurrLabel(getCurrency());
			data.setIncDesc(getDescription());
			data.setSignatureValue(getRequestSignature(sum, invoceId));
			
			*/
			return data;
		} catch (Exception ex) {
			provider.processException(ex, "request");
		}
		return null;
	}

	/**
	 * 
	 * @return MD5 подпись при инициализации оплаты в UpperCase
	 * @throws NoSuchAlgorithmException
	 */
	private String getRequestSignature(String sum, int invoiceId)
			throws NoSuchAlgorithmException {
		// sMerchantLogin:nOutSum:nInvId:sMerchantPass1
		StringBuilder builder = new StringBuilder();
		builder.append(settings.getLogin());
		builder.append(":");
		builder.append(sum);
		builder.append(":");
		builder.append(String.valueOf(invoiceId));
		builder.append(":");
		builder.append(settings.getPass1());
		return getHash(builder);
	}

	/**
	 * Хэш данные который получается при проверке resultUrl
	 * 
	 * @param sum
	 *            сумма платежа
	 * @param invoce
	 *            номер платежа внутри системы
	 * @return Хэш строка в UpperCase
	 * @throws NoSuchAlgorithmException
	 */
	private String getResultHash(String sum, String invoce)
			throws NoSuchAlgorithmException {
		// nOutSum:nInvId:sMerchantPass2
		StringBuilder builder = new StringBuilder();
		builder.append(sum);
		builder.append(":");
		builder.append(invoce);
		builder.append(":");
		builder.append(settings.getPass2());
		return getHash(builder);
	}

	/**
	 * Получить хэш для обработки успешного завершения операции оплаты
	 * 
	 * @param nInvId
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String getSuccessHash(String nInvId, String sum)
			throws NoSuchAlgorithmException {
		// nOutSum:nInvId:sMerchantPass1
		StringBuilder builder = new StringBuilder();
		builder.append(sum);
		builder.append(":");
		builder.append(nInvId);
		builder.append(":");
		builder.append(settings.getPass1());
		return getHash(builder);
	}

	/**
	 * Запрос обрабатывает успешное получение данных на счет фирмы продовца
	 * 
	 * @param data
	 *            данные об успешном завершении операции
	 * @return Успешна ли была операция
	 */
	public boolean success(SuccessInfo data) {

		try {
			String hash = getSuccessHash(data.getInvId(),
					provider.getSum(data.getInvId()));
			String roboHash = data.getSignatureValue().toUpperCase();
			provider.processInfo("success sociomHash: " + hash);
			provider.processInfo("robo incomeHash: " + roboHash);
			if (hash.equals(roboHash)) {
				provider.compleate(data.getInvId());
				return true;
			}
			provider.processSuccessError(data.getInvId());

		} catch (Exception ex) {
			provider.processException(ex, "success");
		}
		return false;

	}

	/**
	 * Метод должен быть использован на resultUrl для подтверждения оплаты
	 * 
	 * @param data
	 *            Данные которые пришли с сервера Robocassa
	 * @returnстроку подтверждения или ошибку
	 * @throws NoSuchAlgorithmException
	 */
	public String сonfirm(ResultInfo data) {

		String result = "FAILED";
		try {

			provider.processInfo("Confirming: " + data.getInvId());
			boolean exist = provider.exist(Integer.parseInt(data.getInvId()));
			provider.processInfo("Exist: " + exist);
			String sociomHash = getResultHash(provider.getSum(data.getInvId()),
					data.getInvId());
			String incomeHash = data.getSignatureValue().toUpperCase();
			provider.processInfo("sociomHash: " + sociomHash);
			provider.processInfo("incomeHash: " + incomeHash);
			if (exist && sociomHash.equals(incomeHash)) {
				result = "OK" + data.getInvId();
			}

		} catch (Exception ex) {
			provider.processException(ex, "confirm");
		}
		return result;
	}

}
