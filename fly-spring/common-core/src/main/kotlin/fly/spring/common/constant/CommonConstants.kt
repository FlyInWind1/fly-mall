/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package fly.spring.common.constant

/**
 * @author lengleng
 * @date 2019/2/1
 */
interface CommonConstants {
    companion object {
        /**
         * 删除
         */
        const val STATUS_DEL = "1"

        /**
         * 正常
         */
        const val STATUS_NORMAL = "0"

        /**
         * 锁定
         */
        const val STATUS_LOCK = "9"

        /**
         * 菜单树根节点
         */
        const val MENU_TREE_ROOT_ID = -1

        /**
         * 菜单
         */
        const val MENU = "0"

        /**
         * 编码
         */
        const val UTF8 = "UTF-8"

        /**
         * JSON 资源
         */
        const val CONTENT_TYPE = "application/json; charset=utf-8"

        /**
         * 前端工程名
         */
        const val FRONT_END_PROJECT = "pig-ui"

        /**
         * 后端工程名
         */
        const val BACK_END_PROJECT = "pig"

        /**
         * 成功标记
         */
        const val SUCCESS = 0

        /**
         * 失败标记
         */
        const val FAIL = 1

        /**
         * 验证码前缀
         */
        const val DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_"

        /**
         * 当前页
         */
        const val CURRENT = "current"

        /**
         * size
         */
        const val SIZE = "size"
    }
}
