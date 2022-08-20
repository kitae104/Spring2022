package kr.inhatc.spring.shop.item.repository;

import static kr.inhatc.spring.shop.item.entity.QItem.item;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.dto.ItemSearchDto;
import kr.inhatc.spring.shop.item.entity.Item;
import kr.inhatc.spring.shop.item.entity.QItem;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom
{

	private JPAQueryFactory queryFactory; // 동적으로 쿼리 생성을 위해 사용

	public ItemRepositoryCustomImpl(EntityManager em)
	{
		this.queryFactory = new JPAQueryFactory(em); // em객체를 이용해서 초기화
	}

	// ==========================================
	// 동적 쿼리 생성 BooleanExpression
	// ==========================================
	/**
	 * 상품 판매 상태 조건<br>
	 * 전체인 경우 null 리턴 - 결과 값이 null이면 where 절에서 해당 조건은 무시<br>
	 * 판매중 또는 품절 상태라면 해당 조건의 상품만 조회
	 * 
	 * @param searchQuery
	 * @return
	 */
	private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus)
	{
		return searchSellStatus == null ? null : item.itemSellStatus.eq(searchSellStatus);
	}

	/**
	 * searchDateType 값에 따라 dateTime의 값을 이전 시간의 값으로 
	 * 세팅한 후 해당 시간 이후로 등록된 상품만 조회
	 * @param searchDateType
	 * @return
	 */
	private BooleanExpression regDateAfter(String searchDateType)
	{
		LocalDateTime dateTime = LocalDateTime.now();

		if (StringUtils.equals("all", searchDateType) || searchDateType == null)
		{
			return null;
		}
		else if(StringUtils.equals("id", searchDateType))
		{
			dateTime = dateTime.minusDays(1);
		}
		else if(StringUtils.equals("iw", searchDateType))
		{
			dateTime = dateTime.minusWeeks(1);
		}
		else if(StringUtils.equals("im", searchDateType))
		{
			dateTime = dateTime.minusMonths(1);
		}
		else if(StringUtils.equals("6m", searchDateType))
		{
			dateTime = dateTime.minusMonths(6);
		}

		return item.regTime.after(dateTime);
	}

	/**
	 * 상품 또는 상품 등록자의 아이디에 검색어를 포함하고 있는 상품을 조회
	 * @param searchBy 검색 종류(상품 / 상품 등록자) 
	 * @param searchQuery 검색어
	 * @return
	 */
	private BooleanExpression searchByLike(String searchBy, String searchQuery)
	{
		if(StringUtils.equals("itemNm", searchBy))
		{
			return item.itemNm.like("%" + searchQuery + "%");
		} 
		else if(StringUtils.equals("createBy", searchBy))
		{
			return item.createdBy.like("%" + searchQuery + "%");
		}
		return null;
	}
	
	@Override
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable)
	{
		List<Item> content = queryFactory.selectFrom(item)
				.where(
					// 상품 판매 상태 	
					searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
					// 기간 
					regDateAfter(itemSearchDto.getSearchDateType()),
					// 상품명 또는 등록자
					searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
				)
				.orderBy(item.id.desc())
				.offset(pageable.getOffset())	// 시작 인덱스 
				.limit(pageable.getPageSize())	// 한 번에 가져올 최대 개수 
				.fetch();
		
		// .fetchResults(); // 더이상 사용 안됨 --> 아래부분 변경

		// List<Item> content = results.getResults();
		// long total = results.getTotal();
		// return new PageImpl<>(content, pageable, total);
		return new PageImpl<>(content, pageable, content.size());
	}

}
