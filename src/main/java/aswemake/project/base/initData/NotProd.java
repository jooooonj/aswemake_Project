package aswemake.project.base.initData;
import aswemake.project.domain.coupon.entity.FixedDiscountCoupon;
import aswemake.project.domain.coupon.entity.PercentageDiscountCoupon;
import aswemake.project.domain.coupon.entity.scopeType.CouponApplyScope;
import aswemake.project.domain.coupon.repository.DiscountCouponRepository;
import aswemake.project.domain.member.entity.Member;
import aswemake.project.domain.member.entity.MemberRoleType;
import aswemake.project.domain.member.repository.MemberRepository;
import aswemake.project.domain.order.entity.request.CreateOrderRequestDto;
import aswemake.project.domain.order.entity.request.OrderItemRequestDto;
import aswemake.project.domain.order.service.OrderService;
import aswemake.project.domain.product.entity.ProductSnapshot;
import aswemake.project.domain.product.entity.request.CreateProductRequestDto;
import aswemake.project.domain.product.entity.response.ProductResponseDto;
import aswemake.project.domain.product.repository.ProductSnapshotRepository;
import aswemake.project.domain.product.service.ProductService;
import aswemake.project.global.config.AppConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Profile({"dev"})
@Transactional
public class NotProd {
    @Bean
    CommandLineRunner initData(
            PasswordEncoder passwordEncoder,
            MemberRepository memberRepository,
            ProductService productService,
            ProductSnapshotRepository productSnapshotRepository,
            OrderService orderService,
            DiscountCouponRepository discountCouponRepository
    ) {
        return args -> {
            //마트관리자
            Member mart = memberRepository.save(
                    Member.builder()
                            .email(AppConfig.getAdminEmail())
                            .password(passwordEncoder.encode(AppConfig.getAdminPassword()))
                            .roleType(MemberRoleType.MART_ADMIN)
                            .build());

            //일반회원
            Member member = memberRepository.save(
                    Member.builder()
                            .email("member1@naver.com")
                            .password(passwordEncoder.encode("12345678"))
                            .roleType(MemberRoleType.MEMBER)
                            .build());

            //상품 생성
            CreateProductRequestDto createProductRequestDto1 =
                    new CreateProductRequestDto("테스트 상품 1", 10000);
            ProductResponseDto product = productService.register(createProductRequestDto1);

            CreateProductRequestDto createProductRequestDto2 =
                    new CreateProductRequestDto("테스트 상품 2", 20000);
            productService.register(createProductRequestDto2);

            //테스트 상품 1에 대한 상품 스냅샷 생성 (2012.12.25 ~ 2012.12.30)
            LocalDateTime fromDate = LocalDateTime.of(2012, 12, 25, 0, 0, 0,0);
            LocalDateTime toDate = LocalDateTime.of(2012, 12, 30, 0, 0, 0,0);

            productSnapshotRepository.save(ProductSnapshot.builder()
                    .productId(product.getId())
                    .fromDate(fromDate)
                    .toDate(toDate)
                    .price(500000)
                    .productName(product.getProductName())
                    .build());


            //주문 생성
            List<OrderItemRequestDto> orderItemRequestDtoList1 =
                    List.of(new OrderItemRequestDto(1L, 5), new OrderItemRequestDto(2L, 2));
            CreateOrderRequestDto createOrderRequestDto1 = new CreateOrderRequestDto(orderItemRequestDtoList1, 3000);
            orderService.createOrder(member.getEmail(), createOrderRequestDto1);

            List<OrderItemRequestDto> orderItemRequestDtoList2 =
                    List.of(new OrderItemRequestDto(1L, 2), new OrderItemRequestDto(2L, 10));
            CreateOrderRequestDto createOrderRequestDto2 = new CreateOrderRequestDto(orderItemRequestDtoList2, 5000);
            orderService.createOrder(member.getEmail(), createOrderRequestDto2);

            //쿠폰생성
            discountCouponRepository.save(
                        FixedDiscountCoupon.createStub(20000, CouponApplyScope.ORDER_ALL, "2만원_고정할인_전체주문"));
            discountCouponRepository.save(
                    FixedDiscountCoupon.createStub(20000, CouponApplyScope.SPECIFIC_PRODUCT, "2만원_고정할인_특정상품"));
            discountCouponRepository.save(
                    PercentageDiscountCoupon.createStub(10, CouponApplyScope.ORDER_ALL, "10퍼센트_비율할인_전체주문"));
            discountCouponRepository.save(
                    PercentageDiscountCoupon.createStub(10, CouponApplyScope.SPECIFIC_PRODUCT, "10퍼센트_비율할인_특정상품"));
        };
    }
}
