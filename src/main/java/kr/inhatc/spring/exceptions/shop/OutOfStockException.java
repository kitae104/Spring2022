package kr.inhatc.spring.exceptions.shop;

public class OutOfStockException extends RuntimeException
{

	public OutOfStockException(String message)
	{
		super(message);
	}
}
