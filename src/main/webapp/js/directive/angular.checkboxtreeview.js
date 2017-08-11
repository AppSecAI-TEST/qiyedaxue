/*
 * 带有复选框的tree
 * */
(function (angular) {
    'use strict';

    angular.module('angularCheckboxtreeview', []).directive('checkboxtreeModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreeModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtree-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';


                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {

                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };

                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {

                                    if (selectedNode.selected) {
                                    	if(scope.itemArr.chooseItem){
                                        	scope.itemArr.chooseItem(selectedNode);
                                        }
                                    	  
                                        selectedNode.selected = undefined;
                                         
                                        //课程分类控制器方法
                                        /*if(scope.tempDeleteItem){
                                            scope.tempDeleteItem(selectedNode);
                                        }*/
                                        //课程分类控制器方法
                                        
                                    } else {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                      
                                        selectedNode.selected = 'selected';
                                        //添加新选中的项
                                        //scope.selectedArr.push(selectedNode.code);

                                        
                                    }
                                   // scope.roleScope = scope.selectedArr.join(",");
                                    //scope[treeId].currentNode = selectedNode;
                                };

                                scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {

                                    if (selectedNode.selected) {
                                    	if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                        //删除之前选中的选项
                                        /*if (scope.selectedArr != undefined && scope.selectedArr.indexOf(selectedNode.nodeId)) {
                                            scope.selectedArr.splice(scope.selectedArr.indexOf(selectedNode.nodeId), 1);
                                        }*/
                                        //课程分类控制器方法
                                       /* if(scope.tempDeleteItem){
                                            scope.tempDeleteItem(selectedNode);
                                        }*/
                                        
                                    } else {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }

                                        selectedNode.selected = 'selected';
                                        //添加新选中的项
                                        //scope.selectedArr.push(selectedNode.code);

                                        
                                    }
                                    //scope.roleScope = scope.selectedArr.join(",");
                                };

                            }

                            element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            .directive('checkboxauthoritytreeModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxauthoritytreeModel;
                       
                        //node id
                        var nodeId = attrs.nodeId || 'authorityTypeName';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'typeFunction';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'list';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            //父节点
                            + ' <input type="checkbox" ng-show="node.authorityTypeId==null" ng-click="' + treeId + '.selectParentCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span style="cursor:default;" ng-show="node.authorityTypeId==null">{{node.' + nodeLabel + '}}</span>'
                            //子节点
                            + ' <input type="checkbox" ng-show="node.authorityTypeId!=null" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span data-ng-class="node.selected" ng-show="node.authorityTypeId!=null" data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxauthoritytree-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';


                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };

                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {
                                if(selectedNode.authorityTypeName!=selectedNode.typeFunction){
                                    if (selectedNode.selected) {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                      
                                    } else {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = 'selected';
                                        
                                    }
                                }
                                };

                                scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {

                                    if (selectedNode.selected) {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                       
                                    } else {
                                    	
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = 'selected';
                                      
                                    }
                                    
                                };
                                scope[treeId].selectParentCheckBox = scope[treeId].selectParentCheckBox || function (selectedNode) {
                                	//全选子权限
                                	if(scope.itemArr2.chooseParentItem){
                                        scope.itemArr2.chooseParentItem(selectedNode);
                                    }
                                    
                                };

                            }

                            element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            .directive('checkboxtreetwoModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreetwoModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-if="node.statuss==\'3\'&&role.roleType==\'2\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <input type="checkbox" ng-if="role.roleType==\'1\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtreetwo-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';

                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                        
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };
                                
                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {  
                                	 //部门级  and  公司节点不支持点击
                                	 if(scope.role.roleType=="2"&&selectedNode.statuss=="2"){
                                		 return;
                                	 }
                            		 if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                                
                                 scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {
                                     if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                             }
                             element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            //培训活动选择所属部门模态框部门树调用
            .directive('checkboxtreedeptModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreedeptModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-if="node.statuss==\'3\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtreedept-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';

                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                        
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };
                                
                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {  
                                	 //部门级  and  公司节点不支持点击
                                	 if(selectedNode.statuss!="3"){
                                		 return;
                                	 }
                            		 if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                                
                                 scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {
                                     if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                             }
                             element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            .directive('checkboxtreedeptsModel',
                    ['$compile',
                        function ($compile) {
                            return {
                                restrict: 'A',
                                link: function (scope, element, attrs) {
                                    //tree id
                                    var treeId = attrs.checkboxtreeId;
                                     
                                    //tree model
                                    var treeModel = attrs.checkboxtreedeptsModel;

                                    //node id
                                    var nodeId = attrs.nodeId || 'code';

                                    //node label
                                    var nodeLabel = attrs.nodeLabel || 'name';

                                    //children
                                    var nodeChildren = attrs.nodeChildren || 'children';

                                    //tree template
                                    var template =
                                        '<ul>'
                                        + ' <li data-ng-repeat="node in ' + treeModel + '" ng-init="node.cancelSelected = cancelSelected; node.cancelSelectedParentNode = cancelSelectedParentNode; node.nodeSelected = nodeSelected; node.cancelSelectedByEmps = cancelSelectedByEmps; initNodeSelected(node);">'
                                        + ' <i class="collapsed glyphicon glyphicon-plus"'
                                        + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                                        + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                                        + ' <i class="expanded glyphicon glyphicon-minus"'
                                        + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                                        + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                                        + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                                        + ' <input type="checkbox" ng-if="node.statuss!=\'0\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected" />'
                                        + ' <span data-ng-class="node.selected"'
                                        + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                                        + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                                        + ' data-checkboxtreedepts-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                                        + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                                        + ' </li>' +
                                        '</ul>';
                                    
                                  
                                    //勾选子部门 
                                    var selectChildrenDept = function(parentNode){
                                    	if(scope.needSelectChildrenNodes && parentNode[nodeChildren] && parentNode[nodeChildren].length > 0){
                                            var tmp;
                                            //遍历子集
                                    		for(var i in parentNode[nodeChildren]){
                                    			tmp = parentNode[nodeChildren][i];
                                    			tmp.selected = parentNode.selected;
                                                scope.itemArr.chooseDeptItem(tmp);
                                                //遍历子集
                                    			if(tmp[nodeChildren] && tmp[nodeChildren].length > 0){
                                    				selectChildrenDept(tmp);
                                    			}
                                    		}
                                    	}
                                    }
                                    //取消子节点
                                    var cancelSelectChildrenDept = function(parentNode, isTemp){
                                        if(scope.needSelectChildrenNodes && parentNode[nodeChildren] && parentNode[nodeChildren].length > 0){
                                    		var tmp;
                                    		for(var i in parentNode[nodeChildren]){
                                    			//勾选子集
                                    			tmp = parentNode[nodeChildren][i];
                                    			tmp.selected = parentNode.selected;
                                    			 
                                                isTemp ? scope.itemArr.tempDeleteDeptItem(tmp) : scope.itemArr.deleteDeptItem(tmp);
                                                 
                                    			if(tmp[nodeChildren] && tmp[nodeChildren].length > 0){
                                    				cancelSelectChildrenDept(tmp, isTemp);
                                    			}
                                    		}
                                    	}
                                    }
                                    //初始化每个节点的附加数据
                                    scope.initNodeSelected = function(node){
                                    	node.treeId = treeId;
                                    	//初始化子部门的上级部门
                                    	if(node[nodeChildren] && node[nodeChildren].length > 0){
                                    		for(var i in node[nodeChildren]){
                                    			node[nodeChildren][i]["parentDept"] = node;
                                    		}
                                    	}
                                    	//同步索引
                                    	var cnode;
                                    	for(var i in scope.itemArr.currChoosedItemArr){
                                    		cnode = scope.itemArr.currChoosedItemArr[i];
                                        	if(node[nodeId] == cnode[nodeId] && cnode["treeId"] == node["treeId"]){
                                        		node.selected = "selected";
                                        		scope.itemArr.currChoosedItemArr[i] = node;
                                        		break;
                                        	}
                                        }
                                    }
                                    //选中节点
                                    scope.nodeSelected = function(){
                            			if(!this.selected){
                                            this.selected = 'selected';
                                            scope.itemArr.chooseDeptItem(this);
                                            selectChildrenDept(this);
                            			}
                                	}
                                    scope.cancelSelected = function(isTemp){
                            			if(this.selected){
                                            this.selected = undefined;
                                            //删除当前节点
                                            isTemp ? scope.itemArr.tempDeleteDeptItem(this) : scope.itemArr.deleteDeptItem(this);
                                            //删除父节点
                                			this.parentDept && this.parentDept.cancelSelectedParentNode && this.parentDept.cancelSelectedParentNode(isTemp);
                                			//删除子节点
                                            cancelSelectChildrenDept(this, isTemp);
                            			}
                                    }
                                    //逐层取消勾选父节点
                                    scope.cancelSelectedParentNode = function(isTemp){
                                    	
                                    	this.parentDept && this.parentDept.cancelSelectedParentNode && this.parentDept.cancelSelectedParentNode(isTemp);
                                    	 
                                    	isTemp ? scope.itemArr.tempDeleteDeptItem(this) : scope.itemArr.deleteDeptItem(this);
                                    }
                                     
                                    //check tree id, tree model
                                    if (treeId && treeModel) {
                                        //root node
                                        if (attrs.angularCheckboxtreeview) {
                                    
                                            scope[treeId] = scope[treeId] || {};

                                            scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                                selectedNode.collapsed = !selectedNode.collapsed;
                                            };
                                            
                                            scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {  
                                            	if(selectedNode.treeId != "epmTree" && selectedNode.treeId != "labelTree"){
                                            		selectedNode.selected = undefined;
                                            	}
                                            	scope.searchByCondition(selectedNode);
                                            };
                                            
                                            scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {
                                            	if (selectedNode.selected) {
                                                    selectedNode.cancelSelected(true);
                                            	} else {
                                                    selectedNode.nodeSelected();
                                            	}
                                            };
                                         }
                                         element.html('').append($compile(template)(scope));
                                    }
                                }
                            };
                        }]);
})(angular);