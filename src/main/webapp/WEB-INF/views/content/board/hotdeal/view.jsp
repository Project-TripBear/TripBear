<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
	
</head>
<body>
	

<div id="main">
<div class="post-container">
    <div class="post-header">
    id: ${id}<br>
작성자(dto.id): ${dto.id}<br>
        <span class="category">${dto.category}</span>
        <h2 class="subject">${dto.subject}</h2>
        <div class="post-meta">
            <span>작성자: <strong>${dto.name}</strong></span>
            <span> | </span>
            <span>등록일: ${dto.regdate}</span>
            <span> | </span>
            <span>상태: ${dto.status}</span>
        </div>
    </div>
    <hr>
    <c:if test="${not empty dto.img}">
        <div class="post-image-container">
            <img src="/trip/asset/place/${dto.img}" id="imgPlace">
        </div>
    </c:if>
    <div class="post-content">
        ${dto.content}
    </div>
    <div class="hotdeal-info">
        <p><strong>핫딜 아이템:</strong> ${dto.itemName}</p>
        <p><strong>가격:</strong> <c:out value="${dto.price}"/>원</p>
        <p><strong>링크:</strong> <a href="${dto.url}" target="_blank">${dto.url}</a></p>
    </div>

    <c:if test="${not empty dto.seq}">
        <div class="post-actions">
            <c:choose>
                <c:when test="${isLiked}">
                    <button type="button" class="like active" id="btnLike" onclick="like(${dto.seq});">좋아요 취소 ❤️</button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="like" id="btnLike" onclick="like(${dto.seq});">좋아요 👍</button>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${isScrapped}">
                    <button type="button" class="scrap active" id="btnScrap" onclick="scrap(${dto.seq});">스크랩 취소 📘</button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="scrap" id="btnScrap" onclick="scrap(${dto.seq});">스크랩 📋</button>
                </c:otherwise>
            </c:choose>
            <button type="button" class="boardReport" id="btnBoardReport" onclick="report(${dto.seq});">신고</button>
        </div>
    </c:if>

    <div class="comment-section">
        <h3>댓글</h3>
        <table id="comment">
            <tbody>
                <c:forEach items="${clist}" var="cdto">
                    <tr>
                        <td class="commentContent">
                            <div>${cdto.content}</div>
                            <div>${cdto.regdate}</div>
                        </td>
                        <td class="commentInfo">
                            <div>
                                <div>${cdto.name}</div>
                                <c:if test="${id != null && id == cdto.id}">
                                    <div>
                                        <span class="material-symbols-outlined" onclick="del(${cdto.seq});">delete</span>
                                        <span class="material-symbols-outlined" onclick="edit(${cdto.seq});">edit_note</span>
                                    </div>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div id="loading" style="text-align: center; display: none;">
        <img src="/trip/asset/images/loading.gif">
    </div>

    <div style="text-align: center; margin-top: 15px;">
        <button type="button" class="comment" id="btnMoreComment">댓글 더보기</button>
    </div>

    <c:if test="${id != null}">
        <form id="addCommentForm">
            <table id="addComment">
                <tr>
                    <td><input type="text" name="content" class="full" required></td>
                    <td><button type="button" class="comment" id="btnAddComment">댓글 쓰기</button></td>
                </tr>
            </table>
        </form>
    </c:if>

    <div class="bottom-buttons">
        <div>
            <button type="button" class="back" onclick="location.href='/trip/board/list.do?column=${column}&word=${word}';">목록보기</button>
        </div>
        <div>
            <c:if test="${id != null && id == dto.id}">
                <button type="button" class="edit primary" onclick="location.href='/trip/board/edit.do?seq=${dto.seq}';">수정하기</button>
                <button type="button" class="del primary" onclick="location.href='/trip/board/del.do?seq=${dto.seq}';">삭제하기</button>
            </c:if>
        </div>
    </div>
</div>
</div>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c7aebadc3646802527c08622383bc565"></script>
	<script>
	
		/* $('#btnAddComment').click(() => {
			
			
			$.ajax({
				type: 'POST',
				url: '/trip/board/addcomment.do',
				data: {
					content: $('input[name=content]').val(),
					bseq: ${dto.seq}
				},
				dataType: 'json',
				success: function(result) {
					
				},
				error: function(a,b,c) {
					console.log(a,b,c);
				}
			}); 
			
			
			$.post('/trip/board/addcomment.do', {
				content: $('input[name=content]').val(),
				bseq: ${dto.seq}
			}, function(result) {
				
				//alert(result.result);
				//alert(result.dto);
				//댓글 목록 갱신
				
				//새로 작성한 댓글을 화면에 동적 추가
				let temp = `
				
					<tr>
						<td class="commentContent">
							<div>\${result.dto.content}</div>
							<div>\${result.dto.regdate}</div>
						</td>
						<td class="commentInfo">
							<div>
								<div>\${result.dto.name}</div>
								<div>
									<span class="material-symbols-outlined" onclick="del(\${result.dto.seq});">delete</span>
									<span class="material-symbols-outlined" onclick="edit(\${result.dto.seq});">edit_note</span>
								</div>
							</div>
						</td>
					</tr>
				
				`;
				
				$('#comment tbody').prepend(temp);
				
				$('input[name=content]').val('');
				
				
			}, 'json').fail(function(a,b,c) {
				console.log(a,b,c);
			});
			
		}); */
		
		$('#btnAddComment').click(() => {
			
		    // 사용자가 입력한 댓글 내용과 게시글 번호를 가져옵니다.
		    const commentContent = $('input[name=content]').val();
		    const boardSeq = ${dto.seq};

		    // 혹시 모를 공백 입력을 방지하기 위해 앞뒤 공백을 제거합니다.
		    if (commentContent.trim() === '') {
		        alert('댓글 내용을 입력해주세요.');
		        return; // 댓글 내용이 없으면 여기서 실행을 멈춥니다.
		    }

		    // $.post를 사용해 서버로 데이터를 전송합니다.
		    $.post('/trip/hotdeal/addcomment', {
		        content: commentContent,
		        bseq: boardSeq
		    }, function(result) {
		        
		        // --- 이 부분이 바로 '새로고침' 역할을 하는 부분입니다 ---
		        // 서버로부터 성공적으로 추가된 댓글 정보(result.dto)를 받아
		        // HTML 형식으로 만듭니다.
		        let temp = `
		            <tr>
		                <td class="commentContent">
		                    <div>\${result.dto.content}</div>
		                    <div>\${result.dto.regdate}</div>
		                </td>
		                <td class="commentInfo">
		                    <div>
		                        <div>\${result.dto.name}</div>
		                        <div>
		                            <span class="material-symbols-outlined" onclick="del(\${result.dto.seq});">delete</span>
		                            <span class="material-symbols-outlined" onclick="edit(\${result.dto.seq});">edit_note</span>
		                        </div>
		                    </div>
		                </td>
		            </tr>
		        `;
		        
		        // 새로 만든 댓글 HTML을 기존 댓글 목록('#comment')의 맨 앞에 추가합니다.
		        $('#comment tbody').prepend(temp);
		        
		        // 댓글 입력창을 깨끗하게 비워줍니다.
		        $('input[name=content]').val('');
		        
		    }, 'json').fail(function(a, b, c) {
		        // 혹시 서버와 통신이 실패하면 콘솔에 에러를 출력합니다.
		        console.log("AJAX 통신 오류:", a, b, c);
		        alert("댓글 등록에 실패했습니다.");
		    });
		    
		});
		
		//처음: 1~10
		//버튼: 11~15
		//버튼: 16~20
		//버튼: 21~25
		
		let begin = 6;
		
		$('#btnMoreComment').click(() => {
			
			$('#loading').show();
			
			setTimeout(more, 1500);
			
		});
		
		function more() {
			
			$.get('/trip/hotdeal/morecomment', {
				bseq: ${dto.seq},
				begin: begin
			}, function (result) {
				
				if (result.length > 0) {
					
					//댓글 5개를 화면에 출력
					result.forEach(obj => {
						
						let temp = `
							
							<tr>
								<td class="commentContent">
									<div>\${obj.content}</div>
									<div>\${obj.regdate}</div>
								</td>
								<td class="commentInfo">
									<div>
										<div>\${obj.name}</div>
							`;
							
							//익명: if ('') > false
							//인증: if ('hong') > true
							
							if ('${id}' && ('${id}' == obj.id)) {
							temp += `	<div>
											<span class="material-symbols-outlined" onclick="del(\${obj.seq});">delete</span>
											<span class="material-symbols-outlined" onclick="edit(\${obj.seq});">edit_note</span>
										</div>
							`;		
							}
										
							temp += `</div>
								</td>
							</tr>
						
						`;
						
						$('#comment tbody').append(temp);
						
					});//for
					
					
					
					
					begin += 5;
					
				} else {
					alert('더 이상 가져올 댓글이 없습니다.');
				}
				
				$('#loading').hide();
				
			}, 'json')
			.fail(function(a,b,c) {
				console.log(a,b,c);
			});
		}
		
		
		function edit(seq) {
			
			$('.commentEditRow').remove();
			
			//let content = '수정할 댓글입니다.';
			let content = $(event.target).parents('tr').children().eq(0).children().eq(0).text();
			
			$(event.target).parents('tr').after(`
					
				<tr class="commentEditRow">
					<td><input type="text" name="content" class="full" required value="\${content}" id="txtComment"></td>
					<td class="commentEdit">
						<span class="material-symbols-outlined" onclick="editComment(\${seq});">edit_square</span>
						<span class="material-symbols-outlined" onclick="$(event.target).parents('tr').remove();">close</span>
					</td>
				</tr>
					
			`);
			
		}
		
		function editComment(seq) {
			
			//alert(seq);
			//alert($('#txtComment').val());
			
			let div = $(event.target).parents('tr').prev().children().eq(0).children().eq(0);
			let tr = $(event.target).parents('tr');
			
			
			$.post('/trip/hotdeal/editcomment', {
				seq: seq,
				content: $('#txtComment').val()
			}, function (result) {
				
				if (result.result == '1') {
					//alert('성공');
					div.text($('#txtComment').val());
					tr.remove();
					
				} else {
					alert('댓글 수정을 실패했습니다.');
				}
				
			}, 'json')
			.fail(function (a,b,c) {
				console.log(a,b,c);
			});
			
		}
		
		function del(seq) {
			
			$('.commentEditRow').remove();
			
			let tr = $(event.target).parents('tr');
			
			if (confirm('삭제하겠습니까?')) {
				
				//$.ajax();
				
				$.post('/trip/hotdeal/delcomment', {
					seq: seq
				}, function (result) {
					
					if (result.result == '1') {
						//alert('성공');
						
						//$(event.target).parents('tr').remove();
						//console.log(event.target);
						tr.remove(); //클로저
						
					} else {
						alert('댓글 삭제를 실패했습니다.');
					}
					
				}, 'json')
				.fail(function(a,b,c) {
					console.log(a,b,c);
				});
				
			}
			
		}
		
		
		function like(seq) {
		    const btnLike = $('#btnLike');
		    let isLiked = btnLike.hasClass('active');

		    $.ajax({
		        type: 'POST',
		        url: '/trip/hotdeal/like',
		        data: {
		            bseq: seq 
		        },
		        success: function(result) {
		            console.log('서버 DB 변경 성공!');
		            if (isLiked) {
		                btnLike.removeClass('active');
		                btnLike.html('좋아요 👍');
		            } else {
		                btnLike.addClass('active');
		                btnLike.html('좋아요 취소 ❤️');
		            }
		        },
		        error: function(a, b, c) {
		            console.log("AJAX 에러 발생:", a, b, c);
		        }
		    });
		}


		function scrap(seq) {
		    const btnScrap = $('#btnScrap');
		    let isScrapped = btnScrap.hasClass('active');

		    $.ajax({
		        type: 'POST',
		        url: '/trip/hotdeal/scrap', // 스크랩 토글 URL
		        data: { bseq: seq },
		        success: function(result) {
		            if (isScrapped) {
		                btnScrap.removeClass('active');
		                btnScrap.html('스크랩 📋');
		            } else {
		                btnScrap.addClass('active');
		                btnScrap.html('스크랩 취소 📘');
		            }
		        },
		        error: function(a,b,c) { console.log(a,b,c); }
		    });
		}
		
		
		function report(seq) {
		    const btnScrap = $('#btnBoardReport');

		    $.ajax({
		        type: 'POST',
		        url: '/trip/hotdeal/report', // 스크랩 토글 URL
		        data: { bseq: seq },
		        success: function(result) {
		        	console.log('성공함');
		        },
		        error: function(a,b,c) { console.log(a,b,c); }
		    });
		}
		
		//... 기존 스크립트 코드 ...

		/* ▼▼▼ 이 코드를 추가해주세요 ▼▼▼ */
		// 댓글 입력창에서 키를 누를 때 이벤트 처리
		$('#addCommentForm input[name=content]').on('keydown', function(event) {
		    
		    // 눌린 키가 엔터(Enter) 키인지 확인 (keyCode 13)
		    if (event.keyCode === 13) {
		        
		        // 1. 엔터 키의 기본 동작(폼 전송)을 막습니다.
		        event.preventDefault(); 
		        
		        // 2. '댓글 쓰기' 버튼을 강제로 클릭시킵니다.
		        $('#btnAddComment').click(); 
		    }
		});
		
	</script>
	
		
</body>
</html>























