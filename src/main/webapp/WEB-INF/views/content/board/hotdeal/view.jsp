<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>     
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/hotdeal.css">

	
</head>
<body>
	

<div class="page-board-view-container"> 
<div id="main">
<div class="post-container">
    <div class="post-header">
       <%-- id: ${id}<br>
작성자(dto.id): ${dto.id}<br>
작성자(dto.seq): ${dto.seq}<br> --%>
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
    <hr class="post-divider"> 
   <div class="images post-images"> 
    <c:forEach var="img" items="${images}">
        <div class="image-item">
            <img src="${pageContext.request.contextPath}/upload/${img.hotdealImageUrl}" 
                 alt="게시글 이미지" class="post-image-thumb">
        </div>
    </c:forEach>
</div>
    <div class="post-content">
        ${dto.content}
    </div>
    <div class="hotdeal-info info-card"> <p><strong>핫딜 아이템:</strong> ${dto.itemName}</p>
        <p><strong>가격:</strong> <c:out value="${dto.price}"/>원</p>
        <p><strong>링크:</strong> <a href="${dto.url}" target="_blank">${dto.url}</a></p>
    </div>

    <c:if test="${not empty dto.seq}">
        <div class="post-actions action-buttons-group"> <c:choose>
                <c:when test="${isLiked}">
                    <button type="button" class="btn like active" id="btnLike" onclick="like(${dto.seq});"> 좋아요 취소 ❤️ (${likeCount})
                    </button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn like" id="btnLike" onclick="like(${dto.seq});">
                        좋아요 👍 (${likeCount})
                    </button>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${isScrapped}">
                    <button type="button" class="btn scrap active" id="btnScrap" onclick="scrap(${dto.seq});">스크랩 취소 📘</button>
                </c:when>
                <c:otherwise>
                    <button type="button" class="btn scrap" id="btnScrap" onclick="scrap(${dto.seq});">스크랩 📋</button>
                </c:otherwise>
            </c:choose>
            <button type="button" class="btn boardReport btn-danger-soft" id="btnBoardReport" onclick="report(${dto.seq});">신고</button> </div>
    </c:if>

    <div class="comment-section">
        <h3>댓글</h3>
        <table id="comment" class="comment-list-table"> <tbody>
                <c:forEach items="${clist}" var="cdto">
                    <tr class="comment-row" id="comment-row-${cdto.seq}"> <td class="commentContent">
                            <div>${cdto.content}</div>
                            <div>${cdto.regdate}</div>
                        </td>
                        <td class="commentInfo">
                            <div>
                             <%-- 작성자(cdto.id): ${cdto.id}<br> --%>
                                <div>${cdto.name}</div>
                                <c:if test="${id != null && id == cdto.id}">
                                    <div class="comment-actions"> 
                                    <span class="material-symbols-outlined" onclick="edit(${cdto.seq});">수정</span>
                                    <span class="material-symbols-outlined" onclick="del(${cdto.seq});">삭제</span>      
                                    </div>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div id="loading" class="loading-spinner"> <img src="${pageContext.request.contextPath}/resources/img/hotdeal/loading.gif" alt="로딩 중"/>
    </div>

    <div class="comment-more-container"> <button type="button" class="btn btn-secondary btn-full" id="btnMoreComment">댓글 더보기</button>
    </div>

    <c:if test="${id != null}">
        <form id="addCommentForm" class="comment-add-form"> <table id="addComment" class="comment-add-table"> <tr>
                    <td><input type="text" name="content" class="form-control full-width" required></td> <td><button type="button" class="btn btn-primary btn-comment-add" id="btnAddComment">댓글 쓰기</button></td> </tr>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
    </c:if>

    <div class="bottom-buttons action-buttons-group"> <div class="left-align">
            <button type="button" class="btn btn-secondary" onclick="location.href='/trip/board/list.do?column=${column}&word=${word}';">목록보기</button>
        </div>
        <div class="right-align">
            <c:if test="${id != null && id == dto.id}">
                <button type="button" class="btn btn-primary" onclick="location.href='/trip/hotdeal/edit.do?seq=${dto.seq}';">수정</button>
                <button type="button" class="btn btn-danger" onclick="location.href='/trip/hotdeal/del.do?seq=${dto.seq}';">삭제</button> </c:if>
        </div>
    </div>
</div>
</div>
</div>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<!-- 	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=c7aebadc3646802527c08622383bc565"></script>
 -->	<script>
 
 $.ajaxSetup({
	    beforeSend: function(xhr) {
	        const token = $("meta[name='_csrf']").attr("content");
	        const header = $("meta[name='_csrf_header']").attr("content");
	        if(token && header) {
	            xhr.setRequestHeader(header, token);
	        }
	    }
	});
 
 let begin = 6;

 $('#btnAddComment').click(() => {
	    const commentContent = $('input[name=content]').val();
	    const boardSeq = ${dto.seq};

	    if (commentContent.trim() === '') {
	        alert('댓글 내용을 입력해주세요.');
	        return;
	    }

	    $.ajax({
	        url: '/trip/hotdeal/addcomment',
	        method: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({
	            content: commentContent,
	            bseq: boardSeq
	        }),
	        dataType: 'json',
	        success: function(result) {
	            
	            if (result.dto) {
	                // 💡 DOM 생성 시, cursor: pointer 스타일과 클래스를 적용합니다.
	                let temp ='<tr class="comment-row" style="cursor: pointer;">' + 
	                    '<td class="commentContent">' +
	                    '<div>' + result.dto.content + '</div>' +
	                    '<div>' + result.dto.regdate + '</div>' +
	                    '</td>' +
	                    '<td class="commentInfo">' +
	                        '<div>' +
	                            '<div>' + result.dto.name + '</div>' +
	                            '<div class="comment-actions">' + 
	                                '<span class="material-symbols-outlined" onclick="edit(' + result.dto.seq + ');">수정</span>' +
	                                '<span class="material-symbols-outlined" onclick="del(' + result.dto.seq + ');">삭제</span>' +
	                            '</div>' +
	                        '</div>' +
	                    '</td>' +
	                '</tr>';
	                
	                $('#comment tbody').prepend(temp);
	                $('input[name=content]').val('');
	            } else {
	                alert("댓글 등록 실패: 서버에서 데이터를 반환하지 않았습니다.");
	                return;
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("Response:", xhr.responseText);
	            alert("댓글 등록에 실패했습니다.");
	        }
	    });
	});

	// ==========================================================
	// 2. 댓글 더보기 (More Comments) - cursor:pointer 및 클래스 적용
	// ==========================================================
	$('#btnMoreComment').click(() => {
	    $('#loading').show();
	    setTimeout(more, 1500);
	});

	function more() {
	    $.ajax({
	        url: '/trip/hotdeal/morecomment',
	        method: 'GET',
	        data: {
	            bseq: ${dto.seq},
	            begin: begin
	        },
	        dataType: 'json',
	        success: function (result) {
	            
	            if (result.length > 0) {
	                result.forEach(obj => {
	                	
	                	// 👇 ============ 디버그 라인 추가 ============ 👇
	                    console.log("AJAX가 받은 댓글 객체(obj):", obj, "| seq 값:", obj.seq);
	                    // 👆 ============ 디버그 라인 추가 ============ 👆
	                	
	                  // 👇 ============ 수정된 디버그 영역 ============ 👇
	                    const jspUseq = '${useq}';      // 1. JSP가 출력한 useq (문자열)
	                    const objId = obj.id;         // 2. JSON으로 받은 id
	                    const objSeq = obj.seq;         // 2. JSON으로 받은 id

	                    console.log('--- [if 조건문 디버깅] ---');
	                    console.log('(A) jspUseq 값:', jspUseq);
	                    console.log('(A) jspUseq 타입:', typeof jspUseq);
	                    
	                    console.log('(B) obj.id 값:', objId);
	                    console.log('(B) obj.id 타입:', typeof objId);
	                    
	                    console.log('(C) obj.seq 값:', objSeq);
	                    console.log('(A) jspUseq 타입:', typeof objSeq);


	                    // 3. 비교 실행 (원래 코드)
	                    const originalComparison = (jspUseq === String(objId));
	                    console.log('비교1 (A === String(B)) 결과:', originalComparison);
	                    
	                    // 4. 비교 실행 (추천 코드)
	                    const recommendedComparison = (Number(jspUseq) == Number(objId));
	                    console.log('비교2 (Number(A) == Number(B)) 결과:', recommendedComparison);
	                    console.log('---------------------------------');  
	                    
	                    let buttonHtml = '';
	          

	                    if ('${useq}' && '${useq}' == String(obj.id)) {  
	                        buttonHtml = 
	                            '<span class="material-symbols-outlined" onclick="edit(' + obj.seq + ')">수정</span>' +
	                            '<span class="material-symbols-outlined" onclick="del(' + obj.seq + ')">삭제</span>';
	                    }

	                    // 💡 newRow 생성 시, 클래스와 인라인 스타일을 모두 추가합니다. (클릭 모션 FIX)
	                    const newRow = $('<tr>')
	                        .addClass('comment-row')
	                        .css('cursor', 'pointer')
	                        .attr('id', 'comment-row-' + obj.seq)
	                        .append(
	                            $('<td>').addClass('commentContent').append(
	                                $('<div>').text(obj.content), 
	                                $('<div>').text(obj.regdate)
	                            )
	                        )
	                        .append(
	                            $('<td>').addClass('commentInfo').append(
	                                $('<div>').append(
	                                    $('<div>').text(obj.name),
	                                    $('<div>').addClass('comment-action-buttons').html(buttonHtml)
	                                )
	                            )
	                        );
	                    
	                    $('#comment tbody').append(newRow);
	                });
	                
	                begin += 5;
	            } else {
	                alert('더 이상 가져올 댓글이 없습니다.');
	            }
	            $('#loading').hide();
	        },
	        error: function(xhr, status, error) {
	            console.log(xhr, status, error);
	        }
	    });
	}

	// ==========================================================
	// 3. 댓글 수정 기능 (Edit Comment) - seq 누락 방지 및 타입 변환
	// ==========================================================
	function edit(seq) { // 👈 event 인수 제거
    $('.commentEditRow').remove();
    
    // seq로 현재 댓글 행을 찾음
    const commentRow = $('#comment-row-' + seq);
    if (!commentRow.length) {
        console.error('댓글 행을 찾을 수 없습니다: ' + seq);
        return;
    }
    
    // 
    let content = commentRow.find('.commentContent div').first().text();
    
    const commentSeq = seq; 

    // 
    commentRow.after(
        '<tr class="commentEditRow">' +
            '<td colspan="2">' + 
                '<div>' +
                    '<input type="text" name="content" class="full" required value="' + content.replace(/"/g, '&quot;') + '" id="txtComment" style="width: 100%; box-sizing: border-box;">' +
                '</div>' +
                '<div style="margin-top: 10px; text-align: right;">' +
                    // 
                    '<button type="button" class="btn btn-primary btn-small" onclick="editComment(' + commentSeq + ');">확인</button>' +
                    // 
                    '<button type="button" class="btn btn-secondary btn-small" onclick="$(\'.commentEditRow\').remove();" style="margin-left: 5px;">닫기</button>' +
                '</div>' +
            '</td>' +
        '</tr>'
    );
}

	function editComment(seq) { // 👈 event 인수 제거
	    const commentSeq = Number(seq);
	    if (isNaN(commentSeq) || commentSeq <= 0) {
	        console.error('댓글 번호가 유효하지 않아 수정 중단:', seq);
	        alert('댓글 번호 정보가 유효하지 않습니다. 수정에 실패했습니다.');
	        return;
	    }
	    
	    // 
	    let commentRow = $('#comment-row-' + seq);
	    let editRow = commentRow.next('.commentEditRow'); // 
	    let newContent = editRow.find('#txtComment').val().trim(); // 
	    
	    if (!newContent) {
	        alert('댓글 내용을 입력해주세요.');
	        return;
	    }
	    
	    $.ajax({
	        url: '/trip/hotdeal/editcomment',
	        method: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({
	            seq: commentSeq, 
	            content: newContent // 👈 
	        }),
	        dataType: 'json',
	        success: function(result) {
	            if (result.result == '1') {
	                commentRow.find('.commentContent div').first().text(newContent);
	                editRow.remove();
	            } else {
	                alert('댓글 수정을 실패했습니다.');
	            }
	        },
	        error: function(xhr, status, error) {
	            alert('댓글 수정 중 오류가 발생했습니다.');
	        }
	    });
	}

	function del(seq) { // 👈 event 인수 제거
	    $('.commentEditRow').remove();
	    let tr = $('#comment-row-' + seq); // 
	    
	    if (confirm('삭제하겠습니까?')) {
	        $.ajax({
	            url: '/trip/hotdeal/delcomment',
	            method: 'POST',
	            contentType: 'application/json',
	            data: JSON.stringify({ seq: seq }),
	            dataType: 'json',
	            success: function(result) {
	                if (result.result == '1') {
	                    tr.remove();
	                } else {
	                    alert('댓글 삭제를 실패했습니다.');
	                }
	            },
	            error: function(xhr, status, error) {
	                console.log(xhr, status, error);
	            }
	        });
	    }
	}

	function like(seq) {
	    const btnLike = $('#btnLike');
	    let isLiked = btnLike.hasClass('active');

	    $.ajax({
	        type: 'POST',
	        url: '/trip/hotdeal/like',
	        contentType: 'application/json',
	        data: JSON.stringify({ bseq: seq }),
	        dataType: 'json',
	        success: function(result) {
	            if (result.result === 'login_required') {
	                alert('로그인이 필요합니다.');
	                return;
	            }
	            
	            console.log('서버 DB 변경 성공!');
	            
	            if (result.action === 'unliked') {
	                btnLike.removeClass('active');
	                btnLike.html('좋아요 👍 (' + result.likeCount + ')');
	            } else {
	                btnLike.addClass('active');
	                btnLike.html('좋아요 취소 ❤️ (' + result.likeCount + ')');
	            }
	        },
	        error: function(xhr, status, error) {
	            console.log("AJAX 에러 발생:", xhr, status, error);
	        }
	    });
	}

	function scrap(seq) {
	    const btnScrap = $('#btnScrap');
	    let isScrapped = btnScrap.hasClass('active');

	    $.ajax({
	        type: 'POST',
	        url: '/trip/hotdeal/scrap',
	        contentType: 'application/json',
	        data: JSON.stringify({ bseq: seq }),
	        dataType: 'json',
	        success: function(result) {
	            if (result.result === 'login_required') {
	                alert('로그인이 필요합니다.');
	                return;
	            }
	            
	            if (result.action === 'unscrapped') {
	                btnScrap.removeClass('active');
	                btnScrap.html('스크랩 📋');
	            } else {
	                btnScrap.addClass('active');
	                btnScrap.html('스크랩 취소 📘');
	            }
	        },
	        error: function(xhr, status, error) {
	            console.log(xhr, status, error);
	        }
	    });
	}

	function report(seq) {
	    const btnScrap = $('#btnBoardReport');

	    $.ajax({
	        type: 'POST',
	        url: '/trip/hotdeal/report',
	        contentType: 'application/json',
	        data: JSON.stringify({ bseq: seq }),
	        dataType: 'json',
	        success: function(result) {
	            console.log('성공함');
	        },
	        error: function(xhr, status, error) {
	            console.log(xhr, status, error);
	        }
	    });
	}

	// 댓글 입력창에서 엔터키 이벤트 처리
	$('#addCommentForm input[name=content]').on('keydown', function(event) {
	    if (event.keyCode === 13) {
	        event.preventDefault();
	        $('#btnAddComment').click();
	    }
	});
	</script>
	
		
</body>
</html>























