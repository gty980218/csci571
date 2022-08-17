var contentTitles=document.getElementById("content_titles");
var contentCompany=document.getElementById("content_company");
var notFoundError=document.getElementById("not_found_error");
var contentSummary=document.getElementById("content_summary");
var contentCharts=document.getElementById("content_charts");
var contentNews=document.getElementById("content_news");
var symbol;



$("#btn1").hover(function(){
  if(!$("#btn1").hasClass("title_btn_focus")){
    $("#btn1").addClass("title_btn_hover");
  }
},function(){
    $("#btn1").removeClass("title_btn_hover");
});

$("#btn2").hover(function(){
  if(!$("#btn2").hasClass("title_btn_focus")){
    $("#btn2").addClass("title_btn_hover");
  }
},function(){
    $("#btn2").removeClass("title_btn_hover");
});

$("#btn3").hover(function(){
  if(!$("#btn3").hasClass("title_btn_focus")){
    $("#btn3").addClass("title_btn_hover");
  }
},function(){
    $("#btn3").removeClass("title_btn_hover");
});

$("#btn4").hover(function(){
  if(!$("#btn4").hasClass("title_btn_focus")){
    $("#btn4").addClass("title_btn_hover");
  }
},function(){
    $("#btn4").removeClass("title_btn_hover");
});

function search()
{
//  1.判空
    reset();
    symbol = document.getElementById("symbol").value;
    getCompany(symbol);
    getSummary(symbol);
    getCharts(symbol);
    getNews(symbol);
}

function sendFirstReq(url,write,show){
    var xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4 && xhr.status==200){
            var json=JSON.parse(xhr.responseText);
            if($.isEmptyObject(json)){
                show(notFoundError,"on");
            }else{
                write(JSON.parse(xhr.responseText));
                show(content_titles,"on");
                show(contentCompany,"on");
                showBtn(1);
            }
        }else{
            //console.error(xhr.statusText);
        }
    }
    xhr.open("GET",url,true);
    xhr.send();
}

function sendReq(url,write,show,element){
    var xhr=new XMLHttpRequest();
    xhr.onreadystatechange=function(){
        if(xhr.readyState==4 && xhr.status==200){
            write(JSON.parse(xhr.responseText));
            show(element,"off");
        }else{
            //console.error(xhr.statusText);
        }
    }
    xhr.open("GET",url,true);
    xhr.send();
}

function show(element,status){
    if(status == "on"){
        element.style.display = "block";
    }else{
        element.style.display = "none";
    }
}

function showCompany(num){
    show(contentCompany,"on");
    show(contentSummary,"off");
    show(contentCharts,"off");
    show(contentNews,"off");
    showBtn(num);
}

function showSummary(num){
    show(contentCompany,"off");
    show(contentSummary,"on");
    show(contentCharts,"off");
    show(contentNews,"off");
    showBtn(num);
}

function showCharts(num){
    show(contentCharts,"on");
    show(contentCompany,"off");
    show(contentSummary,"off");
    show(contentNews,"off");
    showBtn(num);
}

function showNews(num){
    show(contentNews,"on");
    show(contentCharts,"off");
    show(contentCompany,"off");
    show(contentSummary,"off");
    showBtn(num);
}

function showBtn(num){
    for(var i=1;i<=4;i+=1){
        if(num==i){
            $("#btn"+i).removeClass("title_btn_hover");
            $("#btn"+i).addClass("title_btn_focus");
        }else{
            $("#btn"+i).removeClass("title_btn_focus");
        }
    }
}

function setCompany(response){
     $('#com_pic').attr("src", response["logo"]);
     $("#com_name").append(function(){
			return response["name"];
	 });
	 $("#com_symbol").append(function(){
			return response["ticker"];
	 });
	 $("#com_code").append(function(){
			return response["exchange"];
	 });
	 $("#com_date").append(function(){
			return response["ipo"];
	 });
	 $("#com_category").append(function(){
			return response["finnhubIndustry"];
	 });
}
function setSummary(response){
    var change=response["d"];
    var cp=response["dp"];
    var strDate=getMyDate(response["t"]*1000);
    $("#sum_symbol").append(function(){
			return symbol;
	 });
	 $("#sum_day").append(function(){
			return strDate;
	 });
    $("#sum_pc").append(function(){
			return response["pc"];
	 });
	 $("#sum_op").append(function(){
			return response["o"];
	 });
	 $("#sum_hp").append(function(){
			return response["h"];
	 });
	 $("#sum_lp").append(function(){
			return response["l"];
	 });
	 $("#sum_change").append(function(){
			return response["d"];
	 });
	 $("#sum_cp").append(function(){
			return response["dp"];
	 });
	 if(change<0){
	    $('#sum_pic1').attr("src", "static/img/RedArrowDown.png");
	 }else{
	    $('#sum_pic1').attr("src", "static/img/GreenArrowUp.png");
	 }
	 if(cp<0){
	    $('#sum_pic2').attr("src", "static/img/RedArrowDown.png");
	 }else{
	    $('#sum_pic2').attr("src", "static/img/GreenArrowUp.png");
	 }
}

function setRec(response){
    $("#bl2").append(response[0]["strongSell"]);
    $("#bl3").append(response[0]["sell"]);
    $("#bl4").append(response[0]["hold"]);
    $("#bl5").append(response[0]["buy"]);
    $("#bl6").append(response[0]["strongBuy"]);
}

function setCharts(response){
        let tickerName = document.getElementById("symbol").value;
        let date=response["t"];
        let price=response["c"];
        let vol=response["v"];

        // split the data set into close and volume
        let volume = [], close = [], dataLength = date.length;
        let i;
        for (i = 0; i < dataLength; i += 1) {
            close.push([
                date[i]*1000,
                price[i]
            ]);

            volume.push([
                 date[i]*1000,
                 vol[i]
            ]);
        }

        Highcharts.stockChart('charts', {
            stockTools: {
                gui: {
                    enabled: false
                }
            },

            xAxis: {
                type: 'datetime',
                labels: {
                    format: '{value:%e,%b}'
                }
            },

            yAxis: [{
                title: {text: 'Volume'},
                labels: {align: 'left'},
                min: 0,
            }, {
                title: {text: 'Stock Price'},
                opposite: false,
                min: 0,
            }],

            plotOptions: {
                column: {
                    pointWidth: 2,
                    color: '#404040'
                }
            },

            rangeSelector: {
                allButtonsEnabled: true,
                buttons: [{
                    type: 'day',
                    count: 7,
                    text: '7d'
                }, {
                    type: 'day',
                    count: 15,
                    text: '15d'
                }, {
                    type: 'month',
                    count: 1,
                    text: '1m'
                }, {
                    type: 'month',
                    count: 3,
                    text: '3m'
                }, {
                    type: 'month',
                    count: 6,
                    text: '6m'
                }],
                selected: 0,
                inputEnabled: false
            },
            title: {text: 'Stock Price ' + tickerName + ' ' + getNowDate()},
            subtitle: {
                text: '<a href="https://finnhub.io/" target="_blank">Source: Finnhub</a>',
                useHTML: true
            },

            series: [{
                type: 'area',
                name: 'Stock Price',
                data: close,
                yAxis: 1,
                showInNavigator: true,

                tooltip: {
                    valueDecimals: 2
                },
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
            },
                {
                    type: 'column',
                    name: 'Volume',
                    data: volume,
                    yAxis: 0,
                    showInNavigator: false,
                }]
        });
}

function setNews(response){
    var size = response.length;
    for(var i=0;i<5;i+=1){
        var json=response[i];
        var div=$('<div></div>');
        div.addClass("news");
        $('#content_news').append(div);

        var pic=$('<img>');
        pic.addClass("pics");
        pic.attr('src',json["image"]);
        div.append(pic);

        var title=$('<p></p>');
        title.append(json["headline"]);
        title.attr("id","news_title");
        div.append(title);

        var date=$('<p></p>');
        date.append(getMyDate(json["datetime"]*1000));
        date.attr("id","news_date");
        div.append(date);

        var link=$('<a>See Original Post</a>')
        link.attr("href",json["url"]);
        link.attr("target","_blank");
        link.attr("id","news_link");
        div.append(link);
    }
}

function getCompany(symbol){
    sendFirstReq("/get_company/"+symbol,setCompany,show);
}
function getSummary(symbol){
    sendReq("/get_summary/"+symbol,setSummary,show,contentSummary);
    sendReq("/get_rec/"+symbol,setRec,show,contentSummary);
}

function getCharts(symbol){
    sendReq("/get_charts/"+symbol,setCharts,show,contentCharts);
}

function getNews(symbol){
    sendReq("/get_news/"+symbol,setNews,show,contentNews);
}

function resetAll(){
    $("#symbol").val("");
    reset();
}
function reset(){
    resetCompany();
    resetSummary();
    resetRec();
    resetCharts();
    resetNews();
    show(content_titles,"off");
    show(contentCompany,"off");
    show(notFoundError,"off");
    show(contentSummary,"off");
    show(contentCharts,"off");
    show(contentNews,"off");
}
function resetCompany(){
    $("#com_name").empty();
    $("#com_symbol").empty();
    $("#com_code").empty();
    $("#com_date").empty();
    $("#com_category").empty();
    $('#com_pic').attr("src", "");
}
function resetSummary(){
    $("#sum_symbol").empty();
    $("#sum_day").empty();
    $("#sum_pc").empty();
    $("#sum_hp").empty();
    $("#sum_lp").empty();
    $("#sum_change").empty();
    $("#sum_cp").empty();
    $("#sum_op").empty();
}
function resetRec(){
    $("#bl2").empty();
    $("#bl3").empty();
    $("#bl4").empty();
    $("#bl5").empty();
    $("#bl6").empty();
}

function resetCharts(){
    $("#charts").empty();
}

function resetNews(){
    $("#content_news").empty();
}

function getMyDate(timestamp){
    var date=new Date(timestamp);
    var month=new Array();
    month[0]="January";
    month[1]="February";
    month[2]="March";
    month[3]="April";
    month[4]="May";
    month[5]="June";
    month[6]="July";
    month[7]="August";
    month[8]="September";
    month[9]="October";
    month[10]="November";
    month[11]="December";
    return date.getDate()+" "+month[date.getMonth()]+", "+date.getFullYear();
}

function getNowDate(){
    var nowDate = new Date();
     var year = nowDate.getFullYear();
     var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1)
      : nowDate.getMonth() + 1;
     var day = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate
      .getDate();
     return year + "-" + month + "-" + day;
}

 document.getElementById("search").addEventListener("click", function (event) {
        show(notFoundError,"off");
        var tmp=document.getElementById("symbol");
        if (tmp.reportValidity()) {
            search();
        }
 });

 document.getElementById("symbol").addEventListener("input", function (event) {
        show(notFoundError,"off");
 });

document.getElementById("symbol").onkeydown=function(){
        if(event.keyCode==13){
            show(notFoundError,"off");
            var tmp=document.getElementById("symbol");
            if (tmp.reportValidity()) {
                search();
            }
        }
 };