/**
 * Angularjs环境下分页
 * name: pagination
 * Version: 1.0.0
 */
angular.module('pagination', [])
    .directive('pagination', [function () {
        return {
            restrict: 'A',
            template: '<div class="pagination pagination-control pull-right" ng-show="!conf.responsedError && conf.totalItems > 0">' +
            '第<input type="text" ng-model="jumpPageNum"  ng-keyup="jumpToPage($event)"/>页&nbsp;&nbsp;' +
            '每页显示<select ng-model="conf.itemsPerPage" ng-options="option for option in conf.perPageOptions "></select>' +
            '共<strong>{{ conf.totalItems }}</strong>条' +
            '</div>' +
            '<ul class="pagination pull-right" ng-show="!conf.responsedError && conf.totalItems > 0">' +
            '<li ng-class="{disabled: conf.currentPage == 1}" ng-click="firstPage()">' +
            '<a>首页</a>' +
            '</li>' +
            '<li ng-class="{disabled: conf.currentPage == 1}" ng-click="prevPage()"> ' +
            '<a>上一页</a>' +
            '</li>' +
            '<li ng-repeat="item in pageList track by $index" ng-class="{active: item == conf.currentPage, separate: item == \'...\'}" ng-click="changeCurrentPage(item)">' +
            '<a>{{ item }}</a>' +
            '</li>' +
            '<li ng-class="{disabled: conf.currentPage == conf.numberOfPages}" ng-click="nextPage()">' +
            '<a>下一页</a>' +
            '</li>' +
            '<li ng-class="{disabled: conf.currentPage == conf.numberOfPages}" ng-click="lastPage()">' +
            '<a>末页</a>' +
            '</li>' +
            '</ul>' +
            '<div class="pagination pagination-control pull-left" ng-show="conf.responsedError||conf.totalItems <= 0">暂无数据</div>',
            scope: {
                conf: '='
            },
            link: function (scope, element, attrs) {
            	
                // 定义分页的长度必须为奇数 (default:9)
                scope.conf.pagesLength = parseInt(scope.conf.pagesLength) ? parseInt(scope.conf.pagesLength) : 9;
                if (scope.conf.pagesLength % 2 === 0) {
                    // 如果不是奇数的时候处理一下
                    scope.conf.pagesLength = scope.conf.pagesLength - 1;
                }

                // conf.perPageOptions
                if (!scope.conf.perPageOptions) {
                    scope.conf.perPageOptions = [20, 50, 100, '全部'];
                }

                // pageList数组
                function getPagination() {
                    // conf.currentPage
                    scope.conf.currentPage = parseInt(scope.conf.currentPage) ? parseInt(scope.conf.currentPage) : 1;
                    // conf.totalItems
                    scope.conf.totalItems = parseInt(scope.conf.totalItems);
                    // conf.numberOfPages
                    scope.conf.numberOfPages = Math.ceil(scope.conf.totalItems / (scope.conf.itemsPerPage == "全部" ? scope.conf.totalItems : scope.conf.itemsPerPage));
                    if (scope.conf.currentPage < 1) {
                        scope.conf.currentPage = 1;
                    }
                    if (scope.conf.currentPage > scope.conf.numberOfPages) {
                        scope.conf.currentPage = scope.conf.numberOfPages == 0 ? 1 : scope.conf.numberOfPages;
                    }
                    // jumpPageNum
                    scope.jumpPageNum = scope.conf.currentPage;
                    // 对选项进行sort   '全部'?
                    scope.conf.perPageOptions.sort(function (a, b) {
                        return a - b
                    });
                    //存放页码
                    scope.pageList = [];
                    if (scope.conf.numberOfPages <= scope.conf.pagesLength) {
                        // 判断总页数如果小于等于分页的长度，若小于则直接显示
                        for (i = 1; i <= scope.conf.numberOfPages; i++) {
                            scope.pageList.push(i);
                        }
                    } else {
                        // 总页数大于分页长度（此时分为三种情况：1.左边没有...2.右边没有...3.左右都有...）
                        // 计算中心偏移量
                        var offset = (scope.conf.pagesLength - 1) / 2;
                        if (scope.conf.currentPage <= offset) {
                            // 左边没有...
                            for (i = 1; i <= offset + 2; i++) {
                                scope.pageList.push(i);
                            }
                            scope.pageList.push('...');
                            scope.pageList.push(scope.conf.numberOfPages);
                        } else if (scope.conf.currentPage > scope.conf.numberOfPages - offset) {
                            scope.pageList.push(1);
                            scope.pageList.push('...');
                            for (i = offset + 1; i >= 1; i--) {
                                scope.pageList.push(scope.conf.numberOfPages - i);
                            }
                            scope.pageList.push(scope.conf.numberOfPages);
                        } else {
                            // 最后一种情况，两边都有...
                            scope.pageList.push(1);
                            //scope.pageList.push('...');
                            for (i = Math.ceil(offset / 2); i >= 1; i--) {
                            	if((scope.conf.currentPage - i)>2&&i == Math.ceil(offset / 2)){
                            		scope.pageList.push('...');
                            	} 
                            	scope.pageList.push(scope.conf.currentPage - i);
                            }
                            scope.pageList.push(scope.conf.currentPage);
                            for (i = 1; i <= Math.ceil(offset / 2); i++) {
                                scope.pageList.push(scope.conf.currentPage + i);
                                if(i == Math.ceil(offset / 2)&&(scope.conf.currentPage + i)<(scope.conf.numberOfPages-1)){
                                	scope.pageList.push('...');
                                }
                            }
                            //scope.pageList.push('...');
                            scope.pageList.push(scope.conf.numberOfPages);
                        }
                    }
                    
                    if (scope.conf.onChange) {
                    	scope.conf.onChange();
                    }
                    
                    scope.$parent.conf = scope.conf;   
                    
                }

                //监听conf.currentPage和conf.itemsPerPage的变化 变化后重新查询数据
                /*scope.$watch(function () {
                    return scope.conf.currentPage + ' ' + scope.conf.itemsPerPage + ' ' + scope.conf.totalItems;
                }, getPagination);*/
                
                var isFirstQuery = true;
                
                scope.$watch(function () {
                	return scope.conf.itemsPerPage + ' ' + scope.conf.totalItems;
                }, function(){
                	getPagination();
                });
                
                //第一次查询时  数据总数变化  重新查询
                scope.$watch(function () {
                	return scope.conf.currentPage;
                }, function(){
                	if(isFirstQuery){
                		isFirstQuery = false;
                		return;
                	}
                	scope.conf.onChange();
                });

                // 分页工具条函数
                // 变更当前页
                scope.changeCurrentPage = function (item) {
                    if (item == '...') {
                        return;
                    } else {
                        scope.conf.currentPage = item;
                    }
                };
                //首页
                scope.firstPage = function () {
                    if (scope.conf.currentPage > 1) {
                        scope.conf.currentPage = 1;
                    }
                }
                // prevPage
                scope.prevPage = function () {
                    if (scope.conf.currentPage > 1) {
                        scope.conf.currentPage -= 1;
                    }
                };
                // nextPage
                scope.nextPage = function () {
                    if (scope.conf.currentPage < scope.conf.numberOfPages) {
                        scope.conf.currentPage += 1;
                    }
                };
                // lastPage 末页
                scope.lastPage = function () {
                    if (scope.conf.currentPage < scope.conf.numberOfPages) {
                        scope.conf.currentPage = scope.conf.numberOfPages;
                    }
                };
                // 跳转页
                scope.jumpToPage = function () {
                    scope.jumpPageNum = scope.jumpPageNum.replace(/[^0-9]/g, '');
                    if (scope.jumpPageNum !== '') {
                        scope.conf.currentPage = scope.jumpPageNum;
                    }
                };
            }
        };
    }]);
