/*
 * 带有复选框的tree
 * */
(function (angular) {
    'use strict';

    angular.module('angularRadiotreeview', []).directive('radiotreeModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.treeId;

                        //tree model
                        var treeModel = attrs.radiotreeModel;

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
                            + ' <input type="radio" ng-hide="formData&&node.code==formData.classifyId||node.code==formData.questionClassificationId" name="' + treeId + '" ng-click="' + treeId + '.selectRadio(node);" ng-checked="node.selected"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-tree-id="' + treeId + '"'
                            + ' data-radiotree-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';


                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularRadiotreeview) {

                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };

                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {
                                	
                                	
                                	if(scope.formData.classifyId&&selectedNode.code==scope.formData.classifyId){
                                		return;
                                	}
                                	if(scope.formData.questionClassificationId&&selectedNode.code==scope.formData.questionClassificationId){
                                		return;
                                	}
                                    if (selectedNode.selected) 
                                    {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem)
                                        {
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                    	selectedNode.selected = undefined;
                                    } 
                                    else 
                                    {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem)
                                        {
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                    	selectedNode.selected = 'selected';
                                    }
                                    
                                    if (scope[treeId].currentNode && scope[treeId].currentNode.selected&&scope[treeId].currentNode.code!=selectedNode.code) 
                                    {
                                    	scope[treeId].currentNode.selected = undefined;
                                    }
                                     
                                    scope[treeId].currentNode = selectedNode;
                                    
                                   
                                };

                                scope[treeId].selectRadio = scope[treeId].selectRadio || function (selectedNode) {

                                    if (selectedNode.selected) {
                                    	if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                         
                                    } else {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }

                                        selectedNode.selected = 'selected';
                                         
                                    }
                                    
                                    if (scope[treeId].currentNode && scope[treeId].currentNode.selected&&scope[treeId].currentNode.code!=selectedNode.code) 
                                    {
                                    	scope[treeId].currentNode.selected = undefined;
                                    }
                                     
                                    scope[treeId].currentNode = selectedNode;
                                    
                                };

                            }

                            element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }]);
})(angular);