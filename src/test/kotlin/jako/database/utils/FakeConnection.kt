package jako.database.utils

import org.postgresql.PGNotification
import org.postgresql.copy.CopyManager
import org.postgresql.core.*
import org.postgresql.fastpath.Fastpath
import org.postgresql.jdbc.AutoSave
import org.postgresql.jdbc.FieldMetadata
import org.postgresql.jdbc.PreferQueryMode
import org.postgresql.jdbc.TimestampUtils
import org.postgresql.largeobject.LargeObjectManager
import org.postgresql.replication.PGReplicationConnection
import org.postgresql.util.LruCache
import org.postgresql.util.PGobject
import org.postgresql.xml.PGXmlFactoryFactory
import java.sql.*
import java.util.*
import java.util.concurrent.Executor
import java.util.logging.Logger

class FakeConnection: BaseConnection {
    private var autoCommit = true
    private var closed = false
    var rollbackCalledTimes = 0
        private set
    var commitCalledTimes = 0
        private set
    var closeCalledTimes = 0
        private set

    override fun createArrayOf(typeName: String?, elements: Any?): java.sql.Array {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createArrayOf(typeName: String?, elements: Array<out Any>?): java.sql.Array {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getNotifications(): Array<PGNotification> {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getNotifications(timeoutMillis: Int): Array<PGNotification> {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getCopyAPI(): CopyManager {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getLargeObjectAPI(): LargeObjectManager {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getFastpathAPI(): Fastpath {
        throw RuntimeException("It's a fake connection!")
    }

    override fun addDataType(type: String?, className: String?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun addDataType(type: String?, klass: Class<out PGobject>?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setPrepareThreshold(threshold: Int) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getPrepareThreshold(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setDefaultFetchSize(fetchSize: Int) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getDefaultFetchSize(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getBackendPID(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun cancelQuery() {
        throw RuntimeException("It's a fake connection!")
    }

    override fun escapeIdentifier(identifier: String?): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun escapeLiteral(literal: String?): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getPreferQueryMode(): PreferQueryMode {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getAutosave(): AutoSave {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setAutosave(autoSave: AutoSave?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getReplicationAPI(): PGReplicationConnection {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getParameterStatuses(): MutableMap<String, String> {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getParameterStatus(parameterName: String?): String? {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setAdaptiveFetch(adaptiveFetch: Boolean) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getAdaptiveFetch(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun <T : Any?> unwrap(iface: Class<T>?): T {
        throw RuntimeException("It's a fake connection!")
    }

    override fun isWrapperFor(iface: Class<*>?): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun close() {
        closed = true
        closeCalledTimes++
    }

    override fun createStatement(): Statement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int): Statement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createStatement(resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): Statement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(sql: String?): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(sql: String?, resultSetType: Int, resultSetConcurrency: Int): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(
        sql: String?,
        resultSetType: Int,
        resultSetConcurrency: Int,
        resultSetHoldability: Int
    ): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(sql: String?, autoGeneratedKeys: Int): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(sql: String?, columnIndexes: IntArray?): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareStatement(sql: String?, columnNames: Array<out String>?): PreparedStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareCall(sql: String?): CallableStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareCall(sql: String?, resultSetType: Int, resultSetConcurrency: Int): CallableStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun prepareCall(
        sql: String?,
        resultSetType: Int,
        resultSetConcurrency: Int,
        resultSetHoldability: Int
    ): CallableStatement {
        throw RuntimeException("It's a fake connection!")
    }

    override fun nativeSQL(sql: String?): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setAutoCommit(autoCommit: Boolean) {
        this.autoCommit = autoCommit
    }

    override fun getAutoCommit(): Boolean {
        return autoCommit
    }

    override fun commit() {
        commitCalledTimes++
    }

    override fun rollback() {
        rollbackCalledTimes++
    }

    override fun rollback(savepoint: Savepoint?) {
        rollbackCalledTimes++
    }

    override fun isClosed(): Boolean {
        return closed
    }

    override fun getMetaData(): DatabaseMetaData {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setReadOnly(readOnly: Boolean) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun isReadOnly(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setCatalog(catalog: String?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getCatalog(): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setTransactionIsolation(level: Int) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getTransactionIsolation(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getWarnings(): SQLWarning {
        throw RuntimeException("It's a fake connection!")
    }

    override fun clearWarnings() {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getTypeMap(): MutableMap<String, Class<*>> {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setTypeMap(map: MutableMap<String, Class<*>>?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setHoldability(holdability: Int) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getHoldability(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setSavepoint(): Savepoint {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setSavepoint(name: String?): Savepoint {
        throw RuntimeException("It's a fake connection!")
    }

    override fun releaseSavepoint(savepoint: Savepoint?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createClob(): Clob {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createBlob(): Blob {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createNClob(): NClob {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createSQLXML(): SQLXML {
        throw RuntimeException("It's a fake connection!")
    }

    override fun isValid(timeout: Int): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setClientInfo(name: String?, value: String?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setClientInfo(properties: Properties?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getClientInfo(name: String?): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getClientInfo(): Properties {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createStruct(typeName: String?, attributes: Array<out Any>?): Struct {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setSchema(schema: String?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getSchema(): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun abort(executor: Executor?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setNetworkTimeout(executor: Executor?, milliseconds: Int) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getNetworkTimeout(): Int {
        throw RuntimeException("It's a fake connection!")
    }

    override fun execSQLQuery(s: String?): ResultSet {
        throw RuntimeException("It's a fake connection!")
    }

    override fun execSQLQuery(s: String?, resultSetType: Int, resultSetConcurrency: Int): ResultSet {
        throw RuntimeException("It's a fake connection!")
    }

    override fun execSQLUpdate(s: String?) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getQueryExecutor(): QueryExecutor {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getReplicationProtocol(): ReplicationProtocol {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getObject(type: String?, value: String?, byteValue: ByteArray?): Any {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getEncoding(): Encoding {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getTypeInfo(): TypeInfo {
        throw RuntimeException("It's a fake connection!")
    }

    override fun haveMinimumServerVersion(ver: Int): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun haveMinimumServerVersion(ver: Version?): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun encodeString(str: String?): ByteArray {
        throw RuntimeException("It's a fake connection!")
    }

    override fun escapeString(str: String?): String {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getStandardConformingStrings(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getTimestampUtils(): TimestampUtils {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getLogger(): Logger {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getStringVarcharFlag(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getTransactionState(): TransactionState {
        throw RuntimeException("It's a fake connection!")
    }

    override fun binaryTransferSend(oid: Int): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun isColumnSanitiserDisabled(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun addTimerTask(timerTask: TimerTask?, milliSeconds: Long) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun purgeTimerTasks() {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getFieldMetadataCache(): LruCache<FieldMetadata.Key, FieldMetadata> {
        throw RuntimeException("It's a fake connection!")
    }

    override fun createQuery(
        sql: String?,
        escapeProcessing: Boolean,
        isParameterized: Boolean,
        vararg columnNames: String?
    ): CachedQuery {
        throw RuntimeException("It's a fake connection!")
    }

    override fun setFlushCacheOnDeallocate(flushCacheOnDeallocate: Boolean) {
        throw RuntimeException("It's a fake connection!")
    }

    override fun hintReadOnly(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getXmlFactoryFactory(): PGXmlFactoryFactory {
        throw RuntimeException("It's a fake connection!")
    }

    override fun getLogServerErrorDetail(): Boolean {
        throw RuntimeException("It's a fake connection!")
    }
}