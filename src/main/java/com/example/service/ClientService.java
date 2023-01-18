package com.example.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
	private final TalkRepository talkRepository;
	private final MessageRepository messageRepository;
	private final ClientRepository clientRepository;
	private final ProductRepository productRepository;
	private final ClientReqRepository clientReqRepository;
	private final TradeReqRepository tradeReqRepository;

	public ResponseEntity<List<MessageResponseDto>> getMessages(long talkId) {
		List<Message> messages = new ArrayList<>();
		messages = messageRepository.findAllByTalk(talkId);
		List<MessageResponseDto> messageResponseDtos = new ArrayList<>();
		for(Message message : messages) {
			messageResponseDtos.add(new MessageResponseDto(message));
		}
		return (ResponseEntity<List<MessageResponseDto>>) messageResponseDtos;
	}

	public MessageResponseDto sendMessages(long talkId, Client writer, MessageRequestDto messageRequestDto) {
		Talk talk = talkRepository.findById(talkId).orElseThrow(
			() -> new NullPointerException("톡방이 존재하지 않습니다.")
		);
		if(talk.isActivation()) {
			Message message = new Message(talkId,writer, messageRequestDto.getContent());
			messageRepository.save(message);
			return new MessageResponseDto(message);
		} else {
			return new MessageResponseDto("종료된 톡방에는 메시지를 보낼수 없습니다.");
		}
	}

	//프로필 만들기
	@Transactional
	public ProfileUpdateDto.Res updateProfile(ProfileUpdateDto.Req req, Client client){
		client.updateClientProfile(req.getNickname(), req.getImage());
		return new ProfileUpdateDto.Res(client);
	}

	// 프로필 가져오기
	@Transactional
	public ProfileUpdateDto.Res getProfile(Client client){
		return new ProfileUpdateDto.Res(client);
	}

	// 전체 판매상품 목록 조회
	@Transactional(readOnly = true)
	public List<AllProductResponse> getAllProducts() {
		List<Product> AllProducts = productRepository.findAll();
		List<AllProductResponse> AllProductsResponse = new ArrayList<>();
		for (Product product : AllProducts) {
			Client sellers = clientRepository.findById(product.getSellerId()).orElseThrow(
				() -> new NullPointerException()
			);
			AllProductsResponse.add(new AllProductResponse(product, sellers));
		}
		return AllProductsResponse;

	}
	// 전체 판매자 목록 조회
	@Transactional(readOnly = true)
	public List<AllSellerResponse> getAllSellers(Pageable pageable){
		List<Client> sellerList = clientRepository.findAllBy(pageable);
		List<AllSellerResponse> sellerResponseList = new ArrayList<>();
		for (Client client: sellerList){
			if (client.isSeller()) {
				sellerResponseList.add(new AllSellerResponse(client));
			}
		}
		return sellerResponseList;
	}
	// 판매자 정보 조회
	@Transactional
	public SellerResponse getSellerInfo(Long id){
		Client seller = clientRepository.findById(id).orElseThrow(
			()-> new RuntimeException("찾으시는 판매자가 없습니다.")
		);
		return new SellerResponse(seller);
	}

	@Transactional
	public String sendMatching(Long clientId,Long sellerId){
		Client client = clientRepository.findById(clientId).orElseThrow(
				() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
		);
		Client seller = clientRepository.findById(sellerId).orElseThrow(
				() -> new IllegalArgumentException("해당 판매자가 존재하지 않습니다.")
		);
		clientReqRepository.save(new ClientReq(clientId,sellerId));
		return "매칭 요청에 성공했습니다.";
	}


	@Transactional
	public void buyProduct(Long clientId, Long productId){
		Client client = clientRepository.findById(clientId).orElseThrow(
				() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
		);
		Product product = productRepository.findById(productId).orElseThrow(
				() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
		);

		if (client.getPoint() >= product.getPoint()){
			tradeReqRepository.save(new TradeReq(clientId,productId));
		} else throw new IllegalArgumentException("잔액이 부족합니다.");

	}

//    @Transactional
//    public void withdraw(Long clientId, Long productId){
//        Client client = clientRepository.findById(clientId).orElseThrow(
//                () -> new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
//        );
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
//        );
//
//    }

}