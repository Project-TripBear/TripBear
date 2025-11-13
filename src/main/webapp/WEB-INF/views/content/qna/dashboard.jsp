<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%-- Chart.js 라이브러리 로드 --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>

<h1>📊 Q&A 게시판 인기 키워드 대시보드</h1>

<%-- admin2.css의 dashboard-grid-2col 및 stat-card 스타일을 사용합니다. --%>
<div class="dashboard-grid-2col" style="margin-top: 2rem;">

    <div class="stat-card">
        <h3 class="stat-card-title">인기 키워드 TOP 10</h3>
        <div class="simple-list-container">
            
            <c:choose>
                <c:when test="${not empty keywords}">
                    <ul class="simple-list">
                        <c:forEach items="${keywords}" var="item" varStatus="status">
                            <c:if test="${status.index < 10}">
                                <li>
                                    <span style="font-weight: 700; color: var(--primary); margin-right: 10px;">#${status.count}</span> 
                                    <span>${item.keyword}</span>
                                    <span class="list-count">${item.count}건</span>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:otherwise>
                    <p style="text-align: center; color: var(--text-light); padding: 30px 0;">아직 수집된 키워드 데이터가 없습니다.</p>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    
    <div class="chart-container">
        <h3 class="stat-card-title">키워드 사용 빈도 차트</h3>
        <canvas id="keywordChart"></canvas>
        <div class="card-footer-action">
             <a href="<c:url value='/admin/main'/>" class="text-link">관리자 메인으로 돌아가기</a>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const keywordsData = ${keywords != null ? keywords : '[]'};
    
    if (keywordsData.length > 0) {
        // 차트 표시할 데이터 추출 (상위 10개만)
        const labels = keywordsData.slice(0, 10).map(item => item.keyword);
        const data = keywordsData.slice(0, 10).map(item => item.count);
        
        const ctx = document.getElementById('keywordChart').getContext('2d');
        
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '사용 횟수',
                    data: data,
                    backgroundColor: [
                        '#6C9A8B', // primary
                        '#A8D8C7', // primary-light
                        '#FF8C69', // accent
                        '#7D517D',
                        '#4287F5',
                        '#F5C542',
                        '#F54254',
                        '#54F542',
                        '#42F5C5',
                        '#4254F5'
                    ],
                    borderColor: 'var(--primary-dark)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false, // 차트 컨테이너 크기에 맞춤
                scales: {
                    y: {
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: '빈도수 (건)'
                        }
                    },
                    x: {
                        title: {
                            display: true,
                            text: '키워드/태그'
                        }
                    }
                },
                plugins: {
                    legend: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Q&A 게시판 TOP 10 키워드 분석'
                    }
                }
            }
        });
    }
});
</script>