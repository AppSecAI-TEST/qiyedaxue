package com.chinamobo.ue.activity.common;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.type.TypeFactory;


/**
 * 版本: [1.0]
 * 功能说明: 对象转换JSON服务
 *
 * 作者: WangLg
 * 创建时间: 2016年3月11日 下午6:03:40
 */
@Service
public class ObjectConverUtils extends ObjectMapper{
	
	
	//TODO 统一处理异常

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#defaultClassIntrospector()
	 */
	@Override
	protected ClassIntrospector defaultClassIntrospector() {
		// TODO Auto-generated method stub
		return super.defaultClassIntrospector();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#copy()
	 */
	@Override
	public ObjectMapper copy() {
		// TODO Auto-generated method stub
		return super.copy();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_checkInvalidCopy(java.lang.Class)
	 */
	@Override
	protected void _checkInvalidCopy(Class<?> exp) {
		// TODO Auto-generated method stub
		super._checkInvalidCopy(exp);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_newReader(com.fasterxml.jackson.databind.DeserializationConfig)
	 */
	@Override
	protected ObjectReader _newReader(DeserializationConfig config) {
		// TODO Auto-generated method stub
		return super._newReader(config);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_newReader(com.fasterxml.jackson.databind.DeserializationConfig, com.fasterxml.jackson.databind.JavaType, java.lang.Object, com.fasterxml.jackson.core.FormatSchema, com.fasterxml.jackson.databind.InjectableValues)
	 */
	@Override
	protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate,
			FormatSchema schema, InjectableValues injectableValues) {
		// TODO Auto-generated method stub
		return super._newReader(config, valueType, valueToUpdate, schema, injectableValues);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_newWriter(com.fasterxml.jackson.databind.SerializationConfig)
	 */
	@Override
	protected ObjectWriter _newWriter(SerializationConfig config) {
		// TODO Auto-generated method stub
		return super._newWriter(config);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_newWriter(com.fasterxml.jackson.databind.SerializationConfig, com.fasterxml.jackson.core.FormatSchema)
	 */
	@Override
	protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema) {
		// TODO Auto-generated method stub
		return super._newWriter(config, schema);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_newWriter(com.fasterxml.jackson.databind.SerializationConfig, com.fasterxml.jackson.databind.JavaType, com.fasterxml.jackson.core.PrettyPrinter)
	 */
	@Override
	protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
		// TODO Auto-generated method stub
		return super._newWriter(config, rootType, pp);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#version()
	 */
	@Override
	public Version version() {
		// TODO Auto-generated method stub
		return super.version();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#registerModule(com.fasterxml.jackson.databind.Module)
	 */
	@Override
	public ObjectMapper registerModule(Module module) {
		// TODO Auto-generated method stub
		return super.registerModule(module);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#registerModules(com.fasterxml.jackson.databind.Module[])
	 */
	@Override
	public ObjectMapper registerModules(Module... modules) {
		// TODO Auto-generated method stub
		return super.registerModules(modules);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#registerModules(java.lang.Iterable)
	 */
	@Override
	public ObjectMapper registerModules(Iterable<Module> modules) {
		// TODO Auto-generated method stub
		return super.registerModules(modules);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#findAndRegisterModules()
	 */
	@Override
	public ObjectMapper findAndRegisterModules() {
		// TODO Auto-generated method stub
		return super.findAndRegisterModules();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getSerializationConfig()
	 */
	@Override
	public SerializationConfig getSerializationConfig() {
		// TODO Auto-generated method stub
		return super.getSerializationConfig();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getDeserializationConfig()
	 */
	@Override
	public DeserializationConfig getDeserializationConfig() {
		// TODO Auto-generated method stub
		return super.getDeserializationConfig();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getDeserializationContext()
	 */
	@Override
	public DeserializationContext getDeserializationContext() {
		// TODO Auto-generated method stub
		return super.getDeserializationContext();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setSerializerFactory(com.fasterxml.jackson.databind.ser.SerializerFactory)
	 */
	@Override
	public ObjectMapper setSerializerFactory(SerializerFactory f) {
		// TODO Auto-generated method stub
		return super.setSerializerFactory(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getSerializerFactory()
	 */
	@Override
	public SerializerFactory getSerializerFactory() {
		// TODO Auto-generated method stub
		return super.getSerializerFactory();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setSerializerProvider(com.fasterxml.jackson.databind.ser.DefaultSerializerProvider)
	 */
	@Override
	public ObjectMapper setSerializerProvider(DefaultSerializerProvider p) {
		// TODO Auto-generated method stub
		return super.setSerializerProvider(p);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getSerializerProvider()
	 */
	@Override
	public SerializerProvider getSerializerProvider() {
		// TODO Auto-generated method stub
		return super.getSerializerProvider();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getSerializerProviderInstance()
	 */
	@Override
	public SerializerProvider getSerializerProviderInstance() {
		// TODO Auto-generated method stub
		return super.getSerializerProviderInstance();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setMixIns(java.util.Map)
	 */
	@Override
	public ObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins) {
		// TODO Auto-generated method stub
		return super.setMixIns(sourceMixins);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#addMixIn(java.lang.Class, java.lang.Class)
	 */
	@Override
	public ObjectMapper addMixIn(Class<?> target, Class<?> mixinSource) {
		// TODO Auto-generated method stub
		return super.addMixIn(target, mixinSource);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setMixInResolver(com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver)
	 */
	@Override
	public ObjectMapper setMixInResolver(MixInResolver resolver) {
		// TODO Auto-generated method stub
		return super.setMixInResolver(resolver);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#findMixInClassFor(java.lang.Class)
	 */
	@Override
	public Class<?> findMixInClassFor(Class<?> cls) {
		// TODO Auto-generated method stub
		return super.findMixInClassFor(cls);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#mixInCount()
	 */
	@Override
	public int mixInCount() {
		// TODO Auto-generated method stub
		return super.mixInCount();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setMixInAnnotations(java.util.Map)
	 */
	@Override
	public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
		// TODO Auto-generated method stub
		super.setMixInAnnotations(sourceMixins);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getVisibilityChecker()
	 */
	@Override
	public VisibilityChecker<?> getVisibilityChecker() {
		// TODO Auto-generated method stub
		return super.getVisibilityChecker();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setVisibilityChecker(com.fasterxml.jackson.databind.introspect.VisibilityChecker)
	 */
	@Override
	public void setVisibilityChecker(VisibilityChecker<?> vc) {
		// TODO Auto-generated method stub
		super.setVisibilityChecker(vc);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setVisibility(com.fasterxml.jackson.databind.introspect.VisibilityChecker)
	 */
	@Override
	public ObjectMapper setVisibility(VisibilityChecker<?> vc) {
		// TODO Auto-generated method stub
		return super.setVisibility(vc);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setVisibility(com.fasterxml.jackson.annotation.PropertyAccessor, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility)
	 */
	@Override
	public ObjectMapper setVisibility(PropertyAccessor forMethod, Visibility visibility) {
		// TODO Auto-generated method stub
		return super.setVisibility(forMethod, visibility);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getSubtypeResolver()
	 */
	@Override
	public SubtypeResolver getSubtypeResolver() {
		// TODO Auto-generated method stub
		return super.getSubtypeResolver();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setSubtypeResolver(com.fasterxml.jackson.databind.jsontype.SubtypeResolver)
	 */
	@Override
	public ObjectMapper setSubtypeResolver(SubtypeResolver str) {
		// TODO Auto-generated method stub
		return super.setSubtypeResolver(str);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setAnnotationIntrospector(com.fasterxml.jackson.databind.AnnotationIntrospector)
	 */
	@Override
	public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai) {
		// TODO Auto-generated method stub
		return super.setAnnotationIntrospector(ai);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setAnnotationIntrospectors(com.fasterxml.jackson.databind.AnnotationIntrospector, com.fasterxml.jackson.databind.AnnotationIntrospector)
	 */
	@Override
	public ObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI,
			AnnotationIntrospector deserializerAI) {
		// TODO Auto-generated method stub
		return super.setAnnotationIntrospectors(serializerAI, deserializerAI);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy)
	 */
	@Override
	public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s) {
		// TODO Auto-generated method stub
		return super.setPropertyNamingStrategy(s);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getPropertyNamingStrategy()
	 */
	@Override
	public PropertyNamingStrategy getPropertyNamingStrategy() {
		// TODO Auto-generated method stub
		return super.getPropertyNamingStrategy();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include)
	 */
	@Override
	public ObjectMapper setSerializationInclusion(Include incl) {
		// TODO Auto-generated method stub
		return super.setSerializationInclusion(incl);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setPropertyInclusion(com.fasterxml.jackson.annotation.JsonInclude.Value)
	 */
	@Override
	public ObjectMapper setPropertyInclusion(Value incl) {
		// TODO Auto-generated method stub
		return super.setPropertyInclusion(incl);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setDefaultPrettyPrinter(com.fasterxml.jackson.core.PrettyPrinter)
	 */
	@Override
	public ObjectMapper setDefaultPrettyPrinter(PrettyPrinter pp) {
		// TODO Auto-generated method stub
		return super.setDefaultPrettyPrinter(pp);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enableDefaultTyping()
	 */
	@Override
	public ObjectMapper enableDefaultTyping() {
		// TODO Auto-generated method stub
		return super.enableDefaultTyping();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enableDefaultTyping(com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping)
	 */
	@Override
	public ObjectMapper enableDefaultTyping(DefaultTyping dti) {
		// TODO Auto-generated method stub
		return super.enableDefaultTyping(dti);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enableDefaultTyping(com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping, com.fasterxml.jackson.annotation.JsonTypeInfo.As)
	 */
	@Override
	public ObjectMapper enableDefaultTyping(DefaultTyping applicability, As includeAs) {
		// TODO Auto-generated method stub
		return super.enableDefaultTyping(applicability, includeAs);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enableDefaultTypingAsProperty(com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping, java.lang.String)
	 */
	@Override
	public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName) {
		// TODO Auto-generated method stub
		return super.enableDefaultTypingAsProperty(applicability, propertyName);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disableDefaultTyping()
	 */
	@Override
	public ObjectMapper disableDefaultTyping() {
		// TODO Auto-generated method stub
		return super.disableDefaultTyping();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setDefaultTyping(com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder)
	 */
	@Override
	public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer) {
		// TODO Auto-generated method stub
		return super.setDefaultTyping(typer);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#registerSubtypes(java.lang.Class[])
	 */
	@Override
	public void registerSubtypes(Class<?>... classes) {
		// TODO Auto-generated method stub
		super.registerSubtypes(classes);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#registerSubtypes(com.fasterxml.jackson.databind.jsontype.NamedType[])
	 */
	@Override
	public void registerSubtypes(NamedType... types) {
		// TODO Auto-generated method stub
		super.registerSubtypes(types);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getTypeFactory()
	 */
	@Override
	public TypeFactory getTypeFactory() {
		// TODO Auto-generated method stub
		return super.getTypeFactory();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setTypeFactory(com.fasterxml.jackson.databind.type.TypeFactory)
	 */
	@Override
	public ObjectMapper setTypeFactory(TypeFactory f) {
		// TODO Auto-generated method stub
		return super.setTypeFactory(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#constructType(java.lang.reflect.Type)
	 */
	@Override
	public JavaType constructType(Type t) {
		// TODO Auto-generated method stub
		return super.constructType(t);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getNodeFactory()
	 */
	@Override
	public JsonNodeFactory getNodeFactory() {
		// TODO Auto-generated method stub
		return super.getNodeFactory();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setNodeFactory(com.fasterxml.jackson.databind.node.JsonNodeFactory)
	 */
	@Override
	public ObjectMapper setNodeFactory(JsonNodeFactory f) {
		// TODO Auto-generated method stub
		return super.setNodeFactory(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#addHandler(com.fasterxml.jackson.databind.deser.DeserializationProblemHandler)
	 */
	@Override
	public ObjectMapper addHandler(DeserializationProblemHandler h) {
		// TODO Auto-generated method stub
		return super.addHandler(h);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#clearProblemHandlers()
	 */
	@Override
	public ObjectMapper clearProblemHandlers() {
		// TODO Auto-generated method stub
		return super.clearProblemHandlers();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setConfig(com.fasterxml.jackson.databind.DeserializationConfig)
	 */
	@Override
	public ObjectMapper setConfig(DeserializationConfig config) {
		// TODO Auto-generated method stub
		return super.setConfig(config);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setFilters(com.fasterxml.jackson.databind.ser.FilterProvider)
	 */
	@Override
	public void setFilters(FilterProvider filterProvider) {
		// TODO Auto-generated method stub
		super.setFilters(filterProvider);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setFilterProvider(com.fasterxml.jackson.databind.ser.FilterProvider)
	 */
	@Override
	public ObjectMapper setFilterProvider(FilterProvider filterProvider) {
		// TODO Auto-generated method stub
		return super.setFilterProvider(filterProvider);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setBase64Variant(com.fasterxml.jackson.core.Base64Variant)
	 */
	@Override
	public ObjectMapper setBase64Variant(Base64Variant v) {
		// TODO Auto-generated method stub
		return super.setBase64Variant(v);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setConfig(com.fasterxml.jackson.databind.SerializationConfig)
	 */
	@Override
	public ObjectMapper setConfig(SerializationConfig config) {
		// TODO Auto-generated method stub
		return super.setConfig(config);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getFactory()
	 */
	@Override
	public JsonFactory getFactory() {
		// TODO Auto-generated method stub
		return super.getFactory();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getJsonFactory()
	 */
	@Override
	public JsonFactory getJsonFactory() {
		// TODO Auto-generated method stub
		return super.getJsonFactory();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setDateFormat(java.text.DateFormat)
	 */
	@Override
	public ObjectMapper setDateFormat(DateFormat dateFormat) {
		// TODO Auto-generated method stub
		return super.setDateFormat(dateFormat);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getDateFormat()
	 */
	@Override
	public DateFormat getDateFormat() {
		// TODO Auto-generated method stub
		return super.getDateFormat();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setHandlerInstantiator(com.fasterxml.jackson.databind.cfg.HandlerInstantiator)
	 */
	@Override
	public Object setHandlerInstantiator(HandlerInstantiator hi) {
		// TODO Auto-generated method stub
		return super.setHandlerInstantiator(hi);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setInjectableValues(com.fasterxml.jackson.databind.InjectableValues)
	 */
	@Override
	public ObjectMapper setInjectableValues(InjectableValues injectableValues) {
		// TODO Auto-generated method stub
		return super.setInjectableValues(injectableValues);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#getInjectableValues()
	 */
	@Override
	public InjectableValues getInjectableValues() {
		// TODO Auto-generated method stub
		return super.getInjectableValues();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setLocale(java.util.Locale)
	 */
	@Override
	public ObjectMapper setLocale(Locale l) {
		// TODO Auto-generated method stub
		return super.setLocale(l);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#setTimeZone(java.util.TimeZone)
	 */
	@Override
	public ObjectMapper setTimeZone(TimeZone tz) {
		// TODO Auto-generated method stub
		return super.setTimeZone(tz);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.databind.MapperFeature)
	 */
	@Override
	public boolean isEnabled(MapperFeature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#configure(com.fasterxml.jackson.databind.MapperFeature, boolean)
	 */
	@Override
	public ObjectMapper configure(MapperFeature f, boolean state) {
		// TODO Auto-generated method stub
		return super.configure(f, state);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.databind.MapperFeature[])
	 */
	@Override
	public ObjectMapper enable(MapperFeature... f) {
		// TODO Auto-generated method stub
		return super.enable(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.databind.MapperFeature[])
	 */
	@Override
	public ObjectMapper disable(MapperFeature... f) {
		// TODO Auto-generated method stub
		return super.disable(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.databind.SerializationFeature)
	 */
	@Override
	public boolean isEnabled(SerializationFeature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#configure(com.fasterxml.jackson.databind.SerializationFeature, boolean)
	 */
	@Override
	public ObjectMapper configure(SerializationFeature f, boolean state) {
		// TODO Auto-generated method stub
		return super.configure(f, state);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.databind.SerializationFeature)
	 */
	@Override
	public ObjectMapper enable(SerializationFeature f) {
		// TODO Auto-generated method stub
		return super.enable(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.databind.SerializationFeature, com.fasterxml.jackson.databind.SerializationFeature[])
	 */
	@Override
	public ObjectMapper enable(SerializationFeature first, SerializationFeature... f) {
		// TODO Auto-generated method stub
		return super.enable(first, f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.databind.SerializationFeature)
	 */
	@Override
	public ObjectMapper disable(SerializationFeature f) {
		// TODO Auto-generated method stub
		return super.disable(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.databind.SerializationFeature, com.fasterxml.jackson.databind.SerializationFeature[])
	 */
	@Override
	public ObjectMapper disable(SerializationFeature first, SerializationFeature... f) {
		// TODO Auto-generated method stub
		return super.disable(first, f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.databind.DeserializationFeature)
	 */
	@Override
	public boolean isEnabled(DeserializationFeature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#configure(com.fasterxml.jackson.databind.DeserializationFeature, boolean)
	 */
	@Override
	public ObjectMapper configure(DeserializationFeature f, boolean state) {
		// TODO Auto-generated method stub
		return super.configure(f, state);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.databind.DeserializationFeature)
	 */
	@Override
	public ObjectMapper enable(DeserializationFeature feature) {
		// TODO Auto-generated method stub
		return super.enable(feature);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.databind.DeserializationFeature, com.fasterxml.jackson.databind.DeserializationFeature[])
	 */
	@Override
	public ObjectMapper enable(DeserializationFeature first, DeserializationFeature... f) {
		// TODO Auto-generated method stub
		return super.enable(first, f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.databind.DeserializationFeature)
	 */
	@Override
	public ObjectMapper disable(DeserializationFeature feature) {
		// TODO Auto-generated method stub
		return super.disable(feature);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.databind.DeserializationFeature, com.fasterxml.jackson.databind.DeserializationFeature[])
	 */
	@Override
	public ObjectMapper disable(DeserializationFeature first, DeserializationFeature... f) {
		// TODO Auto-generated method stub
		return super.disable(first, f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#configure(com.fasterxml.jackson.core.JsonParser.Feature, boolean)
	 */
	@Override
	public ObjectMapper configure(Feature f, boolean state) {
		// TODO Auto-generated method stub
		return super.configure(f, state);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#configure(com.fasterxml.jackson.core.JsonGenerator.Feature, boolean)
	 */
	@Override
	public ObjectMapper configure(com.fasterxml.jackson.core.JsonGenerator.Feature f, boolean state) {
		// TODO Auto-generated method stub
		return super.configure(f, state);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.core.JsonParser.Feature[])
	 */
	@Override
	public ObjectMapper enable(Feature... features) {
		// TODO Auto-generated method stub
		return super.enable(features);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#enable(com.fasterxml.jackson.core.JsonGenerator.Feature[])
	 */
	@Override
	public ObjectMapper enable(com.fasterxml.jackson.core.JsonGenerator.Feature... features) {
		// TODO Auto-generated method stub
		return super.enable(features);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.core.JsonParser.Feature[])
	 */
	@Override
	public ObjectMapper disable(Feature... features) {
		// TODO Auto-generated method stub
		return super.disable(features);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#disable(com.fasterxml.jackson.core.JsonGenerator.Feature[])
	 */
	@Override
	public ObjectMapper disable(com.fasterxml.jackson.core.JsonGenerator.Feature... features) {
		// TODO Auto-generated method stub
		return super.disable(features);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.core.JsonParser.Feature)
	 */
	@Override
	public boolean isEnabled(Feature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.core.JsonGenerator.Feature)
	 */
	@Override
	public boolean isEnabled(com.fasterxml.jackson.core.JsonGenerator.Feature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#isEnabled(com.fasterxml.jackson.core.JsonFactory.Feature)
	 */
	@Override
	public boolean isEnabled(com.fasterxml.jackson.core.JsonFactory.Feature f) {
		// TODO Auto-generated method stub
		return super.isEnabled(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(com.fasterxml.jackson.core.JsonParser, java.lang.Class)
	 */
	@Override
	public <T> T readValue(JsonParser jp, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(jp, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(JsonParser jp, TypeReference<?> valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(jp, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(JsonParser jp, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(jp, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(com.fasterxml.jackson.core.JsonParser)
	 */
	@Override
	public <T extends TreeNode> T readTree(JsonParser jp) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(jp);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValues(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.core.type.ResolvedType)
	 */
	@Override
	public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readValues(p, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValues(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readValues(p, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValues(com.fasterxml.jackson.core.JsonParser, java.lang.Class)
	 */
	@Override
	public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readValues(p, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValues(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<?> valueTypeRef)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readValues(p, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(java.io.InputStream)
	 */
	@Override
	public JsonNode readTree(InputStream in) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(in);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(java.io.Reader)
	 */
	@Override
	public JsonNode readTree(Reader r) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(r);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(java.lang.String)
	 */
	@Override
	public JsonNode readTree(String content) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(content);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(byte[])
	 */
	@Override
	public JsonNode readTree(byte[] content) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(content);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(java.io.File)
	 */
	@Override
	public JsonNode readTree(File file) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(file);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readTree(java.net.URL)
	 */
	@Override
	public JsonNode readTree(URL source) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		return super.readTree(source);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValue(com.fasterxml.jackson.core.JsonGenerator, java.lang.Object)
	 */
	@Override
	public void writeValue(JsonGenerator g, Object value)
			throws IOException, JsonGenerationException, JsonMappingException {
		// TODO Auto-generated method stub
		super.writeValue(g, value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeTree(com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.core.TreeNode)
	 */
	@Override
	public void writeTree(JsonGenerator jgen, TreeNode rootNode) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		super.writeTree(jgen, rootNode);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeTree(com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.JsonNode)
	 */
	@Override
	public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		super.writeTree(jgen, rootNode);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#createObjectNode()
	 */
	@Override
	public ObjectNode createObjectNode() {
		// TODO Auto-generated method stub
		return super.createObjectNode();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#createArrayNode()
	 */
	@Override
	public ArrayNode createArrayNode() {
		// TODO Auto-generated method stub
		return super.createArrayNode();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#treeAsTokens(com.fasterxml.jackson.core.TreeNode)
	 */
	@Override
	public JsonParser treeAsTokens(TreeNode n) {
		// TODO Auto-generated method stub
		return super.treeAsTokens(n);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#treeToValue(com.fasterxml.jackson.core.TreeNode, java.lang.Class)
	 */
	@Override
	public <T> T treeToValue(TreeNode n, Class<T> valueType) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return super.treeToValue(n, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#valueToTree(java.lang.Object)
	 */
	@Override
	public <T extends JsonNode> T valueToTree(Object fromValue) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.valueToTree(fromValue);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#canSerialize(java.lang.Class)
	 */
	@Override
	public boolean canSerialize(Class<?> type) {
		// TODO Auto-generated method stub
		return super.canSerialize(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#canSerialize(java.lang.Class, java.util.concurrent.atomic.AtomicReference)
	 */
	@Override
	public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause) {
		// TODO Auto-generated method stub
		return super.canSerialize(type, cause);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#canDeserialize(com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public boolean canDeserialize(JavaType type) {
		// TODO Auto-generated method stub
		return super.canDeserialize(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#canDeserialize(com.fasterxml.jackson.databind.JavaType, java.util.concurrent.atomic.AtomicReference)
	 */
	@Override
	public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause) {
		// TODO Auto-generated method stub
		return super.canDeserialize(type, cause);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.File, java.lang.Class)
	 */
	@Override
	public <T> T readValue(File src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.File, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(File src, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.File, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(File src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.net.URL, java.lang.Class)
	 */
	@Override
	public <T> T readValue(URL src, Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.net.URL, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(URL src, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.net.URL, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(URL src, JavaType valueType) throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T readValue(String content, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(content, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.lang.String, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(String content, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(content, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.lang.String, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(String content, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(content, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.Reader, java.lang.Class)
	 */
	@Override
	public <T> T readValue(Reader src, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.Reader, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(Reader src, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.Reader, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(Reader src, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public <T> T readValue(InputStream src, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.InputStream, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(InputStream src, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(java.io.InputStream, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(InputStream src, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], java.lang.Class)
	 */
	@Override
	public <T> T readValue(byte[] src, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], int, int, java.lang.Class)
	 */
	@Override
	public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, offset, len, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(byte[] src, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], int, int, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, offset, len, valueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(byte[] src, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readValue(byte[], int, int, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T readValue(byte[] src, int offset, int len, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super.readValue(src, offset, len, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValue(java.io.File, java.lang.Object)
	 */
	@Override
	public void writeValue(File resultFile, Object value)
			throws IOException, JsonGenerationException, JsonMappingException {
		// TODO Auto-generated method stub
		super.writeValue(resultFile, value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValue(java.io.OutputStream, java.lang.Object)
	 */
	@Override
	public void writeValue(OutputStream out, Object value)
			throws IOException, JsonGenerationException, JsonMappingException {
		// TODO Auto-generated method stub
		super.writeValue(out, value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValue(java.io.Writer, java.lang.Object)
	 */
	@Override
	public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
		// TODO Auto-generated method stub
		super.writeValue(w, value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValueAsString(java.lang.Object)
	 */
	@Override
	public String writeValueAsString(Object value) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return super.writeValueAsString(value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writeValueAsBytes(java.lang.Object)
	 */
	@Override
	public byte[] writeValueAsBytes(Object value) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return super.writeValueAsBytes(value);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer()
	 */
	@Override
	public ObjectWriter writer() {
		// TODO Auto-generated method stub
		return super.writer();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.databind.SerializationFeature)
	 */
	@Override
	public ObjectWriter writer(SerializationFeature feature) {
		// TODO Auto-generated method stub
		return super.writer(feature);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.databind.SerializationFeature, com.fasterxml.jackson.databind.SerializationFeature[])
	 */
	@Override
	public ObjectWriter writer(SerializationFeature first, SerializationFeature... other) {
		// TODO Auto-generated method stub
		return super.writer(first, other);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(java.text.DateFormat)
	 */
	@Override
	public ObjectWriter writer(DateFormat df) {
		// TODO Auto-generated method stub
		return super.writer(df);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithView(java.lang.Class)
	 */
	@Override
	public ObjectWriter writerWithView(Class<?> serializationView) {
		// TODO Auto-generated method stub
		return super.writerWithView(serializationView);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerFor(java.lang.Class)
	 */
	@Override
	public ObjectWriter writerFor(Class<?> rootType) {
		// TODO Auto-generated method stub
		return super.writerFor(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerFor(com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public ObjectWriter writerFor(TypeReference<?> rootType) {
		// TODO Auto-generated method stub
		return super.writerFor(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerFor(com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public ObjectWriter writerFor(JavaType rootType) {
		// TODO Auto-generated method stub
		return super.writerFor(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.core.PrettyPrinter)
	 */
	@Override
	public ObjectWriter writer(PrettyPrinter pp) {
		// TODO Auto-generated method stub
		return super.writer(pp);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithDefaultPrettyPrinter()
	 */
	@Override
	public ObjectWriter writerWithDefaultPrettyPrinter() {
		// TODO Auto-generated method stub
		return super.writerWithDefaultPrettyPrinter();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.databind.ser.FilterProvider)
	 */
	@Override
	public ObjectWriter writer(FilterProvider filterProvider) {
		// TODO Auto-generated method stub
		return super.writer(filterProvider);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.core.FormatSchema)
	 */
	@Override
	public ObjectWriter writer(FormatSchema schema) {
		// TODO Auto-generated method stub
		return super.writer(schema);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.core.Base64Variant)
	 */
	@Override
	public ObjectWriter writer(Base64Variant defaultBase64) {
		// TODO Auto-generated method stub
		return super.writer(defaultBase64);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.core.io.CharacterEscapes)
	 */
	@Override
	public ObjectWriter writer(CharacterEscapes escapes) {
		// TODO Auto-generated method stub
		return super.writer(escapes);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writer(com.fasterxml.jackson.databind.cfg.ContextAttributes)
	 */
	@Override
	public ObjectWriter writer(ContextAttributes attrs) {
		// TODO Auto-generated method stub
		return super.writer(attrs);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithType(java.lang.Class)
	 */
	@Override
	public ObjectWriter writerWithType(Class<?> rootType) {
		// TODO Auto-generated method stub
		return super.writerWithType(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithType(com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public ObjectWriter writerWithType(TypeReference<?> rootType) {
		// TODO Auto-generated method stub
		return super.writerWithType(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#writerWithType(com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public ObjectWriter writerWithType(JavaType rootType) {
		// TODO Auto-generated method stub
		return super.writerWithType(rootType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader()
	 */
	@Override
	public ObjectReader reader() {
		// TODO Auto-generated method stub
		return super.reader();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.DeserializationFeature)
	 */
	@Override
	public ObjectReader reader(DeserializationFeature feature) {
		// TODO Auto-generated method stub
		return super.reader(feature);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.DeserializationFeature, com.fasterxml.jackson.databind.DeserializationFeature[])
	 */
	@Override
	public ObjectReader reader(DeserializationFeature first, DeserializationFeature... other) {
		// TODO Auto-generated method stub
		return super.reader(first, other);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readerForUpdating(java.lang.Object)
	 */
	@Override
	public ObjectReader readerForUpdating(Object valueToUpdate) {
		// TODO Auto-generated method stub
		return super.readerForUpdating(valueToUpdate);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readerFor(com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public ObjectReader readerFor(JavaType type) {
		// TODO Auto-generated method stub
		return super.readerFor(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readerFor(java.lang.Class)
	 */
	@Override
	public ObjectReader readerFor(Class<?> type) {
		// TODO Auto-generated method stub
		return super.readerFor(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readerFor(com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public ObjectReader readerFor(TypeReference<?> type) {
		// TODO Auto-generated method stub
		return super.readerFor(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.node.JsonNodeFactory)
	 */
	@Override
	public ObjectReader reader(JsonNodeFactory f) {
		// TODO Auto-generated method stub
		return super.reader(f);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.core.FormatSchema)
	 */
	@Override
	public ObjectReader reader(FormatSchema schema) {
		// TODO Auto-generated method stub
		return super.reader(schema);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.InjectableValues)
	 */
	@Override
	public ObjectReader reader(InjectableValues injectableValues) {
		// TODO Auto-generated method stub
		return super.reader(injectableValues);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#readerWithView(java.lang.Class)
	 */
	@Override
	public ObjectReader readerWithView(Class<?> view) {
		// TODO Auto-generated method stub
		return super.readerWithView(view);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.core.Base64Variant)
	 */
	@Override
	public ObjectReader reader(Base64Variant defaultBase64) {
		// TODO Auto-generated method stub
		return super.reader(defaultBase64);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.cfg.ContextAttributes)
	 */
	@Override
	public ObjectReader reader(ContextAttributes attrs) {
		// TODO Auto-generated method stub
		return super.reader(attrs);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public ObjectReader reader(JavaType type) {
		// TODO Auto-generated method stub
		return super.reader(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(java.lang.Class)
	 */
	@Override
	public ObjectReader reader(Class<?> type) {
		// TODO Auto-generated method stub
		return super.reader(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#reader(com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public ObjectReader reader(TypeReference<?> type) {
		// TODO Auto-generated method stub
		return super.reader(type);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#convertValue(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T convertValue(Object fromValue, Class<T> toValueType) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.convertValue(fromValue, toValueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#convertValue(java.lang.Object, com.fasterxml.jackson.core.type.TypeReference)
	 */
	@Override
	public <T> T convertValue(Object fromValue, TypeReference<?> toValueTypeRef) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.convertValue(fromValue, toValueTypeRef);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#convertValue(java.lang.Object, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	public <T> T convertValue(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super.convertValue(fromValue, toValueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_convert(java.lang.Object, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	protected Object _convert(Object fromValue, JavaType toValueType) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return super._convert(fromValue, toValueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#generateJsonSchema(java.lang.Class)
	 */
	@Override
	public JsonSchema generateJsonSchema(Class<?> t) throws JsonMappingException {
		// TODO Auto-generated method stub
		return super.generateJsonSchema(t);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#acceptJsonFormatVisitor(java.lang.Class, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper)
	 */
	@Override
	public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
		// TODO Auto-generated method stub
		super.acceptJsonFormatVisitor(type, visitor);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#acceptJsonFormatVisitor(com.fasterxml.jackson.databind.JavaType, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper)
	 */
	@Override
	public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor) throws JsonMappingException {
		// TODO Auto-generated method stub
		super.acceptJsonFormatVisitor(type, visitor);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_serializerProvider(com.fasterxml.jackson.databind.SerializationConfig)
	 */
	@Override
	protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
		// TODO Auto-generated method stub
		return super._serializerProvider(config);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_defaultPrettyPrinter()
	 */
	@Override
	protected PrettyPrinter _defaultPrettyPrinter() {
		// TODO Auto-generated method stub
		return super._defaultPrettyPrinter();
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#createDeserializationContext(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationConfig)
	 */
	@Override
	protected DefaultDeserializationContext createDeserializationContext(JsonParser jp, DeserializationConfig cfg) {
		// TODO Auto-generated method stub
		return super.createDeserializationContext(jp, cfg);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_readValue(com.fasterxml.jackson.databind.DeserializationConfig, com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	protected Object _readValue(DeserializationConfig cfg, JsonParser jp, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super._readValue(cfg, jp, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_readMapAndClose(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	protected Object _readMapAndClose(JsonParser jp, JavaType valueType)
			throws IOException, JsonParseException, JsonMappingException {
		// TODO Auto-generated method stub
		return super._readMapAndClose(jp, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_initForReading(com.fasterxml.jackson.core.JsonParser)
	 */
	@Override
	protected JsonToken _initForReading(JsonParser p) throws IOException {
		// TODO Auto-generated method stub
		return super._initForReading(p);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_unwrapAndDeserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext, com.fasterxml.jackson.databind.DeserializationConfig, com.fasterxml.jackson.databind.JavaType, com.fasterxml.jackson.databind.JsonDeserializer)
	 */
	@Override
	protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, DeserializationConfig config,
			JavaType rootType, JsonDeserializer<Object> deser) throws IOException {
		// TODO Auto-generated method stub
		return super._unwrapAndDeserialize(p, ctxt, config, rootType, deser);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_findRootDeserializer(com.fasterxml.jackson.databind.DeserializationContext, com.fasterxml.jackson.databind.JavaType)
	 */
	@Override
	protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType)
			throws JsonMappingException {
		// TODO Auto-generated method stub
		return super._findRootDeserializer(ctxt, valueType);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.ObjectMapper#_verifySchemaType(com.fasterxml.jackson.core.FormatSchema)
	 */
	@Override
	protected void _verifySchemaType(FormatSchema schema) {
		// TODO Auto-generated method stub
		super._verifySchemaType(schema);
	}

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.core.ObjectCodec#readValue(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.core.type.ResolvedType)
	 */
	
}
