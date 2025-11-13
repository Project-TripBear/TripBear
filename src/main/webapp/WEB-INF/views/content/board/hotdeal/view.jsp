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
작성자(dto.seq): ${dto.seq}<br>
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
        <div class="images">
        <c:forEach var="img" items="${images}">
            <div class="image-item">
                <img src="/trip/resources/upload/${img.hotdealImageUrl}" 
                     alt="게시글 이미지">
            </div>
        </c:forEach>
    </div>
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
                        작성자(cdto.id): ${cdto.id}<br>
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
        <img src="${pageContext.request.contextPath}/resources/img/hotdeal/loading.gif" />
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
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            
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
	            let temp = `
	                <tr>
	                    <td class="commentContent">
	                        <div>${result.dto.content}</div>
	                        <div>${result.dto.regdate}</div>
	                    </td>
	                    <td class="commentInfo">
	                        <div>
	                            <div>${result.dto.name}</div>
	                            <div>
	                                <span class="material-symbols-outlined" onclick="del(${result.dto.seq});">delete</span>
	                                <span class="material-symbols-outlined" onclick="edit(${result.dto.seq});">edit_note</span>
	                            </div>
	                        </div>
	                    </td>
	                </tr>
	            `;
	            $('#comment tbody').prepend(temp);
	            $('input[name=content]').val('');
	        },
	        error: function(xhr, status, error) {
	            console.log("AJAX 통신 오류:", xhr, status, error);
	            alert("댓글 등록에 실패했습니다.");
	        }
	    });
	});

	$('#btnMoreComment').click(() => {
	    $('#loading').show();
	    
	    setTimeout(more, 1500);
	});

	function more() {
		$.ajax({
	        url: '/trip/hotdeal/morecomment',
	        method: 'GET', // or 'POST', Controller 설정에 따라
	        data: {
	            bseq: ${dto.seq},
	            begin: begin
	        },
	        dataType: 'json',
	        success: function (result) {
	            console.log("Received comments:", result);
	            // JSP 변수 ${id}를 직접 참조하여 현재 로그인 ID를 로그에 출력
	            console.log("Current user id:", '${id}'); 
	            
	            if (result.length > 0) {
	                result.forEach(obj => {
	                    console.log("Comment author id:", obj.id);

	                    // 1. 버튼 HTML을 담을 변수 초기화
	                    let buttonHtml = '';
	    				
	                    // 2. JSP 변수 '${id}'와 댓글 작성자 ID(obj.id) 비교
	                    if ('${id}' && ('${id}' === String(obj.id))) { 
	                        buttonHtml = `
	                            <span class="material-symbols-outlined" onclick="del(${obj.seq});">delete</span>
	                            <span class="material-symbols-outlined" onclick="edit(${obj.seq});">edit_note</span>
	                        `;
	                    }

	                    // 3. jQuery를 사용하여 DOM 요소 생성
	                    const contentDiv = $('<div>').text(obj.content);
	                    const regdateDiv = $('<div>').text(obj.regdate);
	                    const commentContentTd = $('<td>').addClass('commentContent').append(contentDiv).append(regdateDiv);

	                    const nameDiv = $('<div>').text(obj.name);
	                    const buttonsDiv = $('<div>').addClass('comment-action-buttons').html(buttonHtml);
	                    const commentInfoTd = $('<td>').addClass('commentInfo').append($('<div>').append(nameDiv).append(buttonsDiv));

	                    const newRow = $('<tr>').append(commentContentTd).append(commentInfoTd);
	                    
	                    // 4. tbody에 추가
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

	function edit(seq) {
	    $('.commentEditRow').remove();
	    let content = $(event.target).parents('tr').children().eq(0).children().eq(0).text();

	    $(event.target).parents('tr').after(`
	        <tr class="commentEditRow">
	            <td><input type="text" name="content" class="full" required value="${content}" id="txtComment"></td>
	            <td class="commentEdit">
	                <span class="material-symbols-outlined" onclick="editComment(${seq});">edit_square</span>
	                <span class="material-symbols-outlined" onclick="$(event.target).parents('tr').remove();">close</span>
	            </td>
	        </tr>
	    `);
	}

	function editComment(seq) {
	    let div = $(event.target).parents('tr').prev().children().eq(0).children().eq(0);
	    let tr = $(event.target).parents('tr');

	    $.ajax({
	        url: '/trip/hotdeal/editcomment',
	        method: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({
	                    seq: seq,
	                    content: $('#txtComment').val()
	                }),	        dataType: 'json',
	        success: function(result) {
	            if (result.result == '1') {
	                div.text($('#txtComment').val());
	                tr.remove();
	            } else {
	                alert('댓글 수정을 실패했습니다.');
	            }
	        },
	        error: function(xhr, status, error) {
	            console.log(xhr, status, error);
	        }
	    });
	}

	function del(seq) {
	    $('.commentEditRow').remove();
	    let tr = $(event.target).parents('tr');
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
	            console.log('서버 DB 변경 성공!');
	            if (isLiked) {
	                btnLike.removeClass('active');
	                btnLike.html('좋아요 👍');
	            } else {
	                btnLike.addClass('active');
	                btnLike.html('좋아요 취소 ❤️');
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
	            if (isScrapped) {
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























