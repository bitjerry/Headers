/**
 * Description:
 *
 * @ProjectName Header
 * @Title TransformScriptModelProcessor
 * @Author Mr.lin
 * @Date 2023-12-26 12:33
 * @Version V1.0.0
 * @Copyright © 2023 by Mr.lin. All rights reserved.
 */
package su.gov.headers.transform;

@FunctionalInterface
public interface TransformScriptModelProcessor<TransformModel> {

    void process(TransformModel transformModel);
}
