package org.robo.data;


/**
 * �?нтерфейс данных полученных с сервиса робокасы
 * 
 * nOutSum -полученная сумма. Сумма будет передана в той валюте, которая была
 * указана при регистрации магазина. Формат представления числа - разделитель
 * точка.
 * 
 * nInvId - номер счета в магазине
 * 
 * sSignatureValue - контрольная сумма MD5 - строка представляющая собой
 * 32-разрядное число в 16-ричной форме и любом регистре (всего 32 символа 0-9,
 * A-F). Формируется по строке, содержащей некоторые параметры, разделенные ':',
 * с добавлением sMerchantPass2 - (устанавливается через интерфейс
 * администрирования) т.е. nOutSum:nInvId:sMerchantPass2[:пользовательские
 * параметры, в отсортированном порядке] К примеру если при инициализации
 * операции были переданы пользовательские параметры shpb=xxx и shpa=yyy то
 * подпись формируется из строки ...:sMerchantPass2:shpa=yyy:shpb=xxx
 * 
 * 
 * Скрипт, находящийся по Result URL должен проверить правильность контрольной
 * суммы и соответствия суммы платежа ожидаемой сумме. При формировании строки
 * подписи учтите формат представления суммы (в строку при проверке подписи
 * вставляйте именно полученное значение, без какого-либо форматирования).
 * 
 * Данный запрос производится после получения денег.
 * 
 * Факт успешности сообщения магазину об исполнении операции определяется по
 * результату, возвращаемому обменному пункту. Результат должен содержать
 * "OKnMerchantInvId", т.е. для счета #5 должен быть возвращен текст "OK5".
 * 
 * С помощью тестового сервера вы можете проверить результат, возвращаемый
 * скриптом, который находится по Result URL.
 * 
 * При работе с реальным сервером, а не с тестовым в случае невозможности
 * связаться со скриптом по адресу Result URL (связь прерывается по time-out-у
 * либо по отсутствию DNS-записи, либо получен не ожидаемый ответ) на
 * email-адрес администратора магазина отправляется письмо и запрос Result URL
 * считается завершенным успешно. В случае систематического отсутствия связи
 * между серверами магазина и обменного пункта лучше использовать метод
 * определения оплаты с применением интерфейсов XML, а самый желательный и
 * защищенный способ - совмещенный.
 * 
 * 
 * @author igor.ch
 * 
 */
public interface ResultInfo extends PaymentInfo {

	/**
	 * 
	 * @return контрольная сумма MD5(обязательный параметр)
	 */
	public String getSignatureValue();

}
