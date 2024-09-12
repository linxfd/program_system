import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);

// 设置全局配置
Vue.prototype.$message = ElementUI.Message;
Vue.prototype.$alert = ElementUI.MessageBox.alert;
Vue.prototype.$notify = ElementUI.Notification;
Vue.prototype.$confirm = ElementUI.MessageBox.confirm;
Vue.prototype.$prompt = ElementUI.MessageBox.prompt;
// Vue.prototype.$Loading = ElementUI.Loading.service;
Vue.prototype.$Loading = ElementUI.Loading


ElementUI.Dialog.props.closeOnClickModal.default = false;