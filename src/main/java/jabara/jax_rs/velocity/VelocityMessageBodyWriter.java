/**
 * 
 */
package jabara.jax_rs.velocity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * Velocityのテンプレートを使ってテキストを返すための{@link MessageBodyWriter}の実装. <br>
 * 
 * @author jabaraster
 */
public class VelocityMessageBodyWriter implements MessageBodyWriter<VelocityTemplate> {

    private static final Charset DEFAULT_ENCODING = Charset.forName("utf-8"); //$NON-NLS-1$

    private final VelocityEngine engine           = createVelocityEngine();

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType)
     */
    @SuppressWarnings("unused")
    @Override
    public long getSize(final VelocityTemplate pT, final Class<?> pType, final Type pGenericType, final Annotation[] pAnnotations,
            final MediaType pMediaType) {
        return -1;
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType)
     */
    @SuppressWarnings("unused")
    @Override
    public boolean isWriteable(final Class<?> pType, final Type pGenericType, final Annotation[] pAnnotations, final MediaType pMediaType) {
        return VelocityTemplate.class.isAssignableFrom(pType);
    }

    /**
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[],
     *      javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
     */
    @SuppressWarnings("unused")
    @Override
    public void writeTo( //
            final VelocityTemplate pT //
            , final Class<?> pType //
            , final Type pGenericType //
            , final Annotation[] pAnnotations //
            , final MediaType pMediaType //
            , final MultivaluedMap<String, Object> pHttpHeaders //
            , final OutputStream pEntityStream) throws IOException, WebApplicationException {

        final String path = pT.getTemplateBase().getPackage().getName().replace('.', '/') + "/" + pT.getTemplatePath(); //$NON-NLS-1$
        final String encoding = "utf-8"; //$NON-NLS-1$

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(pEntityStream, getOutputEncoding()));
            this.engine.mergeTemplate(path, getTemplateEncoding().name(), pT.getContext(), writer);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (final IOException e) {
                    // 無視
                }
            }
        }
    }

    /**
     * テンプレートを読み込むための{@link VelocityEngine}のインスタンスを生成します. <br>
     * デフォルトでは次のような設定を施したインスタンスを生成します. <br>
     * <ul>
     * <li>{@link ClasspathResourceLoader}を使用.</li>
     * <li>ログは出力しない.</li>
     * </ul>
     * 
     * @return デフォルトでは{@link ClasspathResourceLoader}を使ってテンプレートをロードするように構成します. <br>
     *         状況に応じてオーバーライドして下さい. <br>
     */
    @SuppressWarnings("static-method")
    protected VelocityEngine createVelocityEngine() {
        final VelocityEngine ret = new VelocityEngine();
        ret.addProperty(RuntimeConstants.RESOURCE_LOADER, "loader"); //$NON-NLS-1$
        ret.addProperty("loader.resource.loader.class", ClasspathResourceLoader.class.getName()); //$NON-NLS-1$
        ret.addProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        ret.init();
        return ret;
    }

    /**
     * 出力テキストのエンコーディング.
     * 
     * @return デフォルトではUTF-8を返します. <br>
     *         状況に応じてオーバーライドして下さい. <br>
     */
    @SuppressWarnings("static-method")
    protected Charset getOutputEncoding() {
        return DEFAULT_ENCODING;
    }

    /**
     * Velocityテンプレートのエンコーディング.
     * 
     * @return デフォルトではUTF-8を返します. <br>
     *         状況に応じてオーバーライドして下さい. <br>
     */
    @SuppressWarnings("static-method")
    protected Charset getTemplateEncoding() {
        return DEFAULT_ENCODING;
    }
}
